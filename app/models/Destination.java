package models;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.surevine.gateway.rules.RuleFileManager;
import com.surevine.gateway.scm.service.SCMFederatorServiceFacade;
import com.typesafe.config.ConfigFactory;

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

	@Id
	public Long id;

	/**
	 * Human-readable display name for destination
	 */
	@Required
	public String name;

	/**
	 * Location of destination the gateway will send to
	 */
	@Required
	@Column(unique=true)
	public String url;

	/**
	 * SCM projects configured to be shared with destination
	 */
	@ManyToMany(cascade=CascadeType.ALL)
	@JsonManagedReference
	public List<Project> projects = new ArrayList<Project>();

	public static final String DESTINATIONS_RULES_DIRECTORY = ConfigFactory.load().getString("gateway.destinations.rules.dir");
	public static final String DEFAULT_EXPORT_RULEFILE_NAME = "export.js";

    /**
     *  Generic query helper for entity Destination with id Long
     */
    public static Finder<Long,Destination> find = new Finder<Long,Destination>(Long.class, Destination.class);

    /**
     * List of all destinations, used by scala helper in templates (to populate select options)
     * @param project Project that destinations are being added to
     * @return Map<String, String> option key/values
     */
    public static Map<String, String> allDestinationSelectOptions(Project project) {
    	Map<String, String> opts = new HashMap<String, String>();

    	List<Destination> destinations = find.all();

    	for(Destination destination : destinations) {
    		if(!project.destinations.contains(destination)) {
    			opts.put(destination.id.toString(), destination.name);
    		}
    	}

    	return opts;
    }

    public Destination(long id, String name, String url) {
    	this.id = id;
    	this.name = name;
    	this.url = url;
    }

    public Destination(String name, String url) {
    	this.name = name;
    	this.url = url;
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
    public void addProject(Project project) {
    	if(!this.projects.contains(project)) {
        	this.projects.add(project);
        	this.update();

        	SCMFederatorServiceFacade scmFederatorService = new SCMFederatorServiceFacade();
        	scmFederatorService.distribute(this.id.toString(), project.projectKey, project.repositorySlug);
    	}
    }

    /**
     * Removes a project from the destination
     *
     * @param project project to remove
     */
	public void removeProject(Project project) {
		if(this.projects.contains(project)) {
			this.projects.remove(project);
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
     * Perform additional validation (beyond annotations) on model properties.
     *
     * @return List of validation error messages associated with relevant properties
     */
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();

    	// Validate URL
    	final URL u;
    	try {
    		u = new URL(url);
    	}
    	catch(MalformedURLException e) {
    		errors.add(new ValidationError("url", "Valid URL required."));
    	}

    	return errors.isEmpty() ? null : errors;
    }

}
