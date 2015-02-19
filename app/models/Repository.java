package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	 * Configured destinations to federate repo with
	 */
	@OneToMany(cascade=CascadeType.ALL)
	@JsonBackReference
	public List<FederationConfiguration> federationConfigurations = new ArrayList<FederationConfiguration>();

    /**
     * Generic query helper for entity Project with id Long
     */
    public static final Model.Finder<Long,Repository> FIND = new Model.Finder<Long,Repository>(Long.class, Repository.class);

    public Repository(RepositoryType repoType, String identifier) {
    	this.repoType = repoType;
    	this.identifier = identifier;
    }

    /**
     * List of all projects, used by scala helper in templates (to populate select options)
     * @return Map<String, String> option key/values
     */
    public static List<Repository> allShareOptions(Destination destination) {
    	// TODO improve to only include unshared repos
    	return FIND.all();
    }

    /**
     * Retrieve list of destinations the repository is shared with
     * @return
     */
	public List<Destination> getSharedDestinations() {
		List<Destination> sharedDestinations = new ArrayList<Destination>();
		for(FederationConfiguration config : federationConfigurations) {
			sharedDestinations.add(config.destination);
		}
		return sharedDestinations;
	}

}
