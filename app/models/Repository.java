package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * A repository that can be shared across the Gateway.
 * @author jonnyheavey
 */
@Entity
public class Repository extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@Required
	@Enumerated(EnumType.STRING)
	public RepositoryType repoType;

	/**
	 * Unique identifier for repository from host system
	 */
	@Required
	@MaxLength(255)
	public String identifier;

	/**
	 * Destinations the repository is shared with
	 */
	@ManyToMany(mappedBy = "repositories", cascade=CascadeType.ALL)
	@JsonBackReference
	public List<Destination> destinations = new ArrayList<Destination>();

    /**
     * Generic query helper for entity Project with id Long
     */
    public static final Model.Finder<Long,Repository> FIND = new Model.Finder<Long,Repository>(Long.class, Repository.class);

    public Repository(RepositoryType repoType, String identifier) {
    	this.repoType = repoType;
    	this.identifier = identifier;
    }

}
