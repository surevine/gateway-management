package models;

import static play.mvc.Http.Status.OK;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Expr;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.surevine.gateway.scm.service.SCMFederatorServiceFacade;
import com.typesafe.config.ConfigFactory;

import play.Logger;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

/**
 * Project maintained in SCM system to be shared across gateway.
 *
 * @author jonnyheavey
 *
 */
@Entity
public class Project extends Model {

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
    public static Finder<Long,Project> find = new Finder<Long,Project>(Long.class, Project.class);

    /**
     * List of all projects, used by scala helper in templates (to populate select options)
     * @return Map<String, String> option key/values
     */
    public static List<Project> allProjectShareOptions(Destination destination) {
    	List<Project> unsharedProjects = new ArrayList<Project>();

    	List<Project> allProjects = find.all();

    	for(Project project : allProjects) {
    		if(!destination.projects.contains(project)) {
    			unsharedProjects.add(project);
    		}
    	}

    	return unsharedProjects;
    }

	public Project(long id, String displayName, String projectKey, String repositorySlug) {
    	this.id = id;
    	this.displayName = displayName;
    	this.projectKey = projectKey;
    	this.repositorySlug = repositorySlug;
    }

    public Project(String displayName, String projectKey, String repositorySlug) {
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
        	SCMFederatorServiceFacade scmFederatorService = new SCMFederatorServiceFacade();
        	scmFederatorService.distribute(destination.id.toString(), this.projectKey, this.repositorySlug);
    	}
    }

    /**
     * Perform additional validation (beyond annotations) on model properties.
     *
     * @return String validation error message
     */
    public List<ValidationError> validate() {

    	List<ValidationError> errors = new ArrayList<ValidationError>();

    	Project existingProject = find.where()
    									.eq("projectKey", projectKey)
    									.eq("repositorySlug", repositorySlug)
    									.not(Expr.eq("id", id))
    									.findUnique();

    	if(existingProject != null) {
    		errors.add(new ValidationError("projectKey", "Project key / Repository slug combination already exists."));
    		errors.add(new ValidationError("repositorySlug", "Project key / Repository slug combination already exists."));
    	}

    	return errors.isEmpty() ? null : errors;
    }

}
