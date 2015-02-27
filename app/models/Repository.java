package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;

import com.avaje.ebean.Expr;
import com.fasterxml.jackson.annotation.JsonBackReference;

import play.data.validation.ValidationError;
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
	@Pattern(regexp="^[-_a-zA-Z0-9/]+$",
				message="Invalid identifier: only letters, numbers, forward-slashes, hyphens and underscores supported.")
	public String identifier;

	/**
	 * Configured partners to federate repo with
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
    public static List<Repository> getFederationOptions(Partner partner, String filterType) {

		List<Long> federatedRepositoryIds = new ArrayList<Long>();
		for(FederationConfiguration config : partner.federationConfigurations) {
			federatedRepositoryIds.add(config.repository.id);
		}

    	if(filterType != null) {
    		RepositoryType repoFilterType = RepositoryType.valueOf(filterType);
    		return FIND.where()
    				.not(Expr.in("id", federatedRepositoryIds))
    				.eq("repoType", repoFilterType).findList();
    	} else {
    		return FIND.where().not(Expr.in("id", federatedRepositoryIds)).findList();
    	}

    }

    /**
     * Retrieve list of partners the repository is shared with
     * @return
     */
	public List<Partner> getPartners() {
		List<Partner> sharedPartners = new ArrayList<Partner>();
		for(FederationConfiguration config : federationConfigurations) {
			sharedPartners.add(config.partner);
		}
		return sharedPartners;
	}

    /**
     * Perform additional validation (beyond annotations) on model properties.
     *
     * @return List of validation error messages associated with relevant properties
     */
    public List<ValidationError> validate() {
    	List<ValidationError> errors = new ArrayList<ValidationError>();

    	// Ensure Repository unique
    	Repository existingRepository = FIND.where()
    											.eq("repoType", repoType)
    											.eq("identifier", identifier)
    											.not(Expr.eq("id", id))
    											.findUnique();
    	if(existingRepository != null) {
    		errors.add(new ValidationError("", "Repository with type/identifier combination already exists."));
    	}

    	return errors.isEmpty() ? null : errors;
    }

}
