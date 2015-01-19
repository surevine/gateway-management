package models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Expr;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.surevine.gateway.scm.service.SCMFederatorServiceFacade;

import play.data.validation.ValidationError;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Project maintained in SCM system to be shared across gateway.
 *
 * @author jonnyheavey
 *
 */
@Entity
public class OutboundProject extends Model {

    private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	/**
	 * Human readable name for display in console UI
	 */
	@Required
	@MaxLength(255)
	public String displayName;

	/**
	 * 'Project' segment of SCM system's repository URL
	 * e.g. github.com/project/repository
	 */
	@Required
	@MaxLength(255)
	public String projectKey;

	/**
	 * 'Repository' segment of SCM system's repository URL
	 * e.g. github.com/project/repository
	 */
	@Required
	@MaxLength(255)
	public String repositorySlug;

	/**
	 * Destinations the project is configured to be shared with
	 */
	@ManyToMany(mappedBy = "projects", cascade=CascadeType.ALL)
	@JsonBackReference
	public List<Destination> destinations = new ArrayList<Destination>();

    /**
     * Generic query helper for entity Project with id Long
     */
    public static Model.Finder<Long,OutboundProject> find = new Model.Finder<Long,OutboundProject>(Long.class, OutboundProject.class);

    /**
     * Service facade for interaction with SCM federator component
     */
    public static SCMFederatorServiceFacade scmFederator = SCMFederatorServiceFacade.getInstance();

    /**
     * List of all projects, used by scala helper in templates (to populate select options)
     * @return Map<String, String> option key/values
     */
    public static List<OutboundProject> allProjectShareOptions(Destination destination) {
    	List<OutboundProject> unsharedProjects = new ArrayList<OutboundProject>();

    	List<OutboundProject> allProjects = find.all();

    	for(OutboundProject project : allProjects) {
    		if(!destination.projects.contains(project)) {
    			unsharedProjects.add(project);
    		}
    	}

    	return unsharedProjects;
    }

	public OutboundProject(long id, String displayName, String projectKey, String repositorySlug) {
    	this.id = id;
    	this.displayName = displayName;
    	this.projectKey = projectKey;
    	this.repositorySlug = repositorySlug;
    }

    public OutboundProject(String displayName, String projectKey, String repositorySlug) {
    	this.displayName = displayName;
    	this.projectKey = projectKey;
    	this.repositorySlug = repositorySlug;
    }

    /**
     * Adds destination to share project to
     *
     * @param destination destination to share to
     */
    public void addDestination(Destination destination) {
    	if(!this.destinations.contains(destination)) {
    		this.destinations.add(destination);
    		this.update();

        	scmFederator.distribute(destination.id.toString(), this.projectKey, this.repositorySlug);
    	}
    }

    /**
     * Perform additional validation (beyond annotations) on model properties.
     *
     * @return String validation error message
     */
    public List<ValidationError> validate() {

    	List<ValidationError> errors = new ArrayList<ValidationError>();

    	OutboundProject existingProject = find.where()
    									.eq("projectKey", projectKey)
    									.eq("repositorySlug", repositorySlug)
    									.not(Expr.eq("id", id))
    									.findUnique();

    	if(existingProject != null) {
    		errors.add(new ValidationError("projectKey", "Project key / Repository slug combination already exists."));
    		errors.add(new ValidationError("repositorySlug", "Project key / Repository slug combination already exists."));
    	}

    	Pattern p = Pattern.compile("([^-a-zA-Z0-9])");
    	Matcher projectKeyMatcher = p.matcher(projectKey);
        if (projectKeyMatcher.find()) {
        	errors.add(new ValidationError("projectKey", "Project key contains invalid characters."));
        }
    	Matcher repoSlugMatcher = p.matcher(repositorySlug);
        if (repoSlugMatcher.find()) {
        	errors.add(new ValidationError("repositorySlug", "Repository slug contains invalid characters."));
        }

    	return errors.isEmpty() ? null : errors;
    }

	public void setSCMFederatorServiceFacade(SCMFederatorServiceFacade scmFederator) {
		OutboundProject.scmFederator = scmFederator;
	}

}
