package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Represents a configured federation (share)
 * of repository to partner
 * @author jonnyheavey
 */
@Entity
public class FederationConfiguration extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@Required
	@ManyToOne
	@JsonManagedReference
	public Partner partner;

	@Required
	@ManyToOne
	@JsonManagedReference
	public Repository repository;

	@Required
	public boolean inboundEnabled;

	@Required
	public boolean outboundEnabled;

    /**
     * Generic query helper for entity Federation with id Long
     */
    public static final Model.Finder<Long,FederationConfiguration> FIND = new Model.Finder<Long,FederationConfiguration>(Long.class, FederationConfiguration.class);

	public FederationConfiguration(Partner partner,
									Repository repository,
									boolean inboundEnabled,
									boolean outboundEnabled) {

		this.partner = partner;
		this.repository = repository;
		this.inboundEnabled = inboundEnabled;
		this.outboundEnabled = outboundEnabled;
	}

	public void setInboundEnabled(boolean enabled) {
		this.inboundEnabled = enabled;
	}

	public void setOutboundEnabled(boolean enabled) {
		this.outboundEnabled = enabled;
	}

}
