package models;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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

	@Required
	public String name;

	@Required
	@Column(unique=true)
	public String url;

	@ManyToMany(mappedBy = "projects")
	public List<Destination> destinations = new ArrayList<Destination>();

    /**
     * Generic query helper for entity Computer with id Long
     */
    public static Finder<Long,Project> find = new Finder<Long,Project>(Long.class, Project.class);

	public Project(long id, String name, String url) {
    	this.id = id;
    	this.name = name;
    	this.url = url;
    }

    public Project(String name, String url) {
    	this.name = name;
    	this.url = url;
    }

    /**
     * Perform additional validation (beyond annotations) on model properties.
     *
     * @return List of validation error messages associated with relevant properties
     */
    public List<ValidationError> validate() {

    	List<ValidationError> errors = new ArrayList<ValidationError>();

    	if(!this.url.matches("(\\w+://)(.+@)*([\\w\\d\\.]+)(:[\\d]+){0,1}/*(.*)")) {
    		errors.add(new ValidationError("url", "Valid URL required."));
    	}

    	return errors.isEmpty() ? null : errors;

    }

}
