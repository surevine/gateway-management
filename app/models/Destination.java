package models;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.avaje.ebean.Expr;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.surevine.gateway.federation.FederatorServiceException;
import com.surevine.gateway.federation.issuetracking.IssueTrackingFederatorServiceFacade;
import com.surevine.gateway.rules.RuleFileManager;
import com.surevine.gateway.scm.service.SCMFederatorServiceFacade;
import com.typesafe.config.ConfigFactory;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import play.db.ebean.Model;

/**
 * Gateway destination end-point to share resources with.
 *
 * @author jonnyheavey (jonny.heavey@surevine.com)
 *
 */
@Entity
public class Destination extends Model {

    private static final long serialVersionUID = 1L;
	public static final String DESTINATIONS_RULES_DIRECTORY = ConfigFactory.load().getString("gateway.destinations.rules.dir");
	public static final String DEFAULT_EXPORT_RULEFILE_NAME = "export.js";

	@Id
	public Long id;

	/**
	 * Human-readable display name for destination
	 */
	@Required
	@MaxLength(255)
	public String name;

	/**
	 * Location of destination the gateway will send to
	 */
	@Required
	@MaxLength(255)
	@Column(unique=true)
	public String url;

	/**
	 * Key used by partner/destination when exporting items via gateway.
	 * This is configured in community portal federator components.
	 */
	@Required
	@MaxLength(255)
	public String sourceKey;

	/**
	 * SCM projects configured to be shared with destination
	 */
	@ManyToMany(cascade=CascadeType.ALL)
	@JsonManagedReference
	public List<OutboundProject> projects = new ArrayList<OutboundProject>();

	/**
	 * Issue projects configured to be shared with destination
	 */
	@ManyToMany(cascade=CascadeType.ALL)
	@JsonManagedReference
	public List<OutboundIssueProject> issueProjects = new ArrayList<OutboundIssueProject>();

	/**
	 * Repositories configured to be shared to/from destination
	 */
	@ManyToMany(cascade=CascadeType.ALL)
	@JsonManagedReference
	public List<Repository> repositories = new ArrayList<Repository>();

	/**
	 * Configured repositories to federate to destination
	 */
	@OneToMany(cascade=CascadeType.ALL)
	@JsonBackReference
	public List<FederationConfiguration> federationConfigurations = new ArrayList<FederationConfiguration>();

    /**
     * Generic query helper for entity Destination with id Long
     */
    public static final Model.Finder<Long,Destination> FIND = new Model.Finder<Long,Destination>(Long.class, Destination.class);

    public Destination(String name, String url) {
    	this.name = name;
    	this.url = url;
    }

    public Destination(long id, String name, String url) {
    	this.id = id;
    	this.name = name;
    	this.url = url;
    }

    public Destination(long id, String name, String url, List<OutboundProject> projects) {
    	this.id = id;
    	this.name = name;
    	this.url = url;
    	this.projects = projects;
    }

    /**
     * List of all destinations, used by scala helper in templates (to populate select options)
     * @param project Project that destinations are being added to
     * @return Map<String, String> option key/values
     */
    public static List<Destination> allDestinationShareOptions(OutboundProject project) {
    	List<Destination> unsharedDestinations = new ArrayList<Destination>();

    	List<Destination> allDestinations = FIND.all();

    	for(Destination destination : allDestinations) {
    		if(!project.destinations.contains(destination)) {
    			unsharedDestinations.add(destination);
    		}
    	}

    	return unsharedDestinations;
    }

    public static List<Destination> allDestinationIssueShareOptions(OutboundIssueProject project) {
    	List<Destination> unsharedDestinations = new ArrayList<Destination>();

    	List<Destination> allDestinations = FIND.all();

    	for(Destination destination : allDestinations) {
    		if(!project.destinations.contains(destination)) {
    			unsharedDestinations.add(destination);
    		}
    	}

    	return unsharedDestinations;
    }

    @Override
    public void save() {
    	super.save();
    	RuleFileManager ruleFileManager = RuleFileManager.getInstance();
    	ruleFileManager.createDestinationRuleFileDirectory(this);
    	ruleFileManager.createDestinationRuleFile(this, DEFAULT_EXPORT_RULEFILE_NAME);
    }

    @Override
    public void delete() {
    	RuleFileManager.getInstance().deleteDestinationRuleFileDirectory(this);
    	super.delete();
    }

    /**
     * Adds a project to the destination
     *
     * @param project project to add
     */
    public void addProject(OutboundProject project) {
    	if(!this.projects.contains(project)) {
        	this.projects.add(project);
        	this.update();

        	SCMFederatorServiceFacade.getInstance().distribute(this.id.toString(), project.projectKey, project.repositorySlug);
    	}
    }

    /**
     * Adds an issue project to the destination
     *
     * @param project project to add
     */
    public void addIssueProject(OutboundIssueProject project) {
    	if(!this.issueProjects.contains(project)) {
        	this.issueProjects.add(project);
        	this.update();

    		try {
				IssueTrackingFederatorServiceFacade.getInstance().distribute(this.id.toString(), project.projectKey);
			} catch (FederatorServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}
    }

    /**
     * Removes a project from the destination
     *
     * @param project project to remove
     */
	public void removeProject(OutboundProject project) {
		if(this.projects.contains(project)) {
			this.projects.remove(project);
			this.update();
		}
	}

    /**
     * Removes an issue project from the destination
     *
     * @param project project to remove
     */
	public void removeIssueProject(OutboundIssueProject project) {
		if(this.issueProjects.contains(project)) {
			this.issueProjects.remove(project);
			this.update();
		}
	}

	/**
     * Loads rules for the destination from expected rule-file locations
     *
     * @return String contents of rule file
     * @throws IOException
     */
    public String loadRules() throws IOException {
    	return RuleFileManager.getInstance().loadDestinationExportRules(this);
    }

    /**
     * Updates destination rule file contents
     * @param ruleFileContent String contents for file
     * @throws IOException
     */
	public void updateRules(String ruleFileContent) throws IOException {
		RuleFileManager.getInstance().updateDestinationRuleFile(this, ruleFileContent);
	}

    /**
     * Perform additional validation (beyond annotations) on model properties.
     *
     * @return List of validation error messages associated with relevant properties
     */
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();

    	// Validate URL
    	try {
    		new URL(url);
    	} catch(MalformedURLException e) {
    		errors.add(new ValidationError("url", "Valid URL required."));
    	}

    	// Ensure URL unique
    	Destination existingDestination = FIND.where()
    											.eq("url", url)
    											.not(Expr.eq("id", id))
    											.findUnique();
    	if(existingDestination != null) {
    		errors.add(new ValidationError("url", "Destination URL already exists."));
    	}

    	return errors.isEmpty() ? null : errors;
    }

}
