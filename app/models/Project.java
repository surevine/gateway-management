package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import play.data.validation.ValidationError;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

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
	public String displayName;

	/**
	 * 'Project' segment of SCM system's repository URL
	 * e.g. github.com/project/repository
	 */
	@Required
	public String projectSlug;

	/**
	 * 'Repository' segment of SCM system's repository URL
	 * e.g. github.com/project/repository
	 */
	@Required
	public String repositorySlug;

	/**
	 * Destinations the project is configured to be shared with
	 */
	@ManyToMany(mappedBy = "projects")
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
    public static Map<String, String> allProjectSelectOptions() {
    	Map<String, String> opts = new HashMap<String, String>();

    	List<Project> projects = find.all();

    	for(Project project : projects) {
    		opts.put(project.id.toString(), project.displayName);
    	}

    	return opts;
    }

	public Project(long id, String displayName, String projectSlug, String repositorySlug) {
    	this.id = id;
    	this.displayName = displayName;
    	this.projectSlug = projectSlug;
    	this.repositorySlug = repositorySlug;
    }

    public Project(String displayName, String projectSlug, String repositorySlug) {
    	this.displayName = displayName;
    	this.projectSlug = projectSlug;
    	this.repositorySlug = repositorySlug;
    }

    /**
     * Perform additional validation (beyond annotations) on model properties.
     *
     * @return List of validation error messages associated with relevant properties
     */
    public List<ValidationError> validate() {

    	List<ValidationError> errors = new ArrayList<ValidationError>();

    	// TODO

    	return errors.isEmpty() ? null : errors;

    }

}
