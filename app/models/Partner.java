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
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;

import com.avaje.ebean.Expr;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.surevine.gateway.rules.RuleFileManager;
import com.typesafe.config.ConfigFactory;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
import play.db.ebean.Model;

/**
 * Gateway partner representing to share (and receive) resources with.
 *
 * @author jonnyheavey (jonny.heavey@surevine.com)
 *
 */
@Entity
public class Partner extends Model {

    private static final long serialVersionUID = 1L;
	public static final String PARTNERS_RULES_DIRECTORY = ConfigFactory.load().getString("gateway.partners.rules.dir");
	public static final String DEFAULT_EXPORT_RULEFILE_NAME = "export.js";

	@Id
	public Long id;

	/**
	 * Human-readable display name for partner
	 */
	@Required
	@MaxLength(255)
	public String name;

	/**
	 * Location of partner the gateway will send to
	 */
	@Required
	@MaxLength(255)
	@Column(unique=true)
	public String url;

	/**
	 * Key used by partner when exporting items via gateway.
	 * This is configured in community portal federator components.
	 */
	@Required
	@MaxLength(255)
	@Column(unique=true)
	@Pattern(regexp="^[-_a-zA-Z0-9]+$",
				message="Invalid source key: only letters, numbers, hyphens and underscores supported.")
	public String sourceKey;

	/**
	 * Configured repositories to federate to partner
	 */
	@OneToMany(cascade=CascadeType.ALL)
	@JsonBackReference
	public List<FederationConfiguration> federationConfigurations = new ArrayList<FederationConfiguration>();

    /**
     * Generic query helper for entity Partner with id Long
     */
    public static final Model.Finder<Long,Partner> FIND = new Model.Finder<Long,Partner>(Long.class, Partner.class);

    public Partner(String name, String url, String sourceKey) {
    	this.name = name;
    	this.url = url;
    }

    public Partner(long id, String name, String url, String sourceKey) {
    	this.id = id;
    	this.name = name;
    	this.url = url;
    }

    public Partner(long id, String name, String url) {
    	this(id, name, url, "");
    }

    @Override
    public void save() {
    	super.save();
    	RuleFileManager ruleFileManager = RuleFileManager.getInstance();
    	ruleFileManager.createPartnerRuleFileDirectory(this);
    	ruleFileManager.createPartnerRuleFile(this, DEFAULT_EXPORT_RULEFILE_NAME);
    }

    @Override
    public void delete() {
    	RuleFileManager.getInstance().deletePartnerRuleFileDirectory(this);
    	super.delete();
    }

	/**
     * Loads rules for the partner from expected rule-file locations
     *
     * @return String contents of rule file
     * @throws IOException
     */
    public String loadRules() throws IOException {
    	return RuleFileManager.getInstance().loadPartnerExportRules(this);
    }

    /**
     * Updates partner rule file contents
     * @param ruleFileContent String contents for file
     * @throws IOException
     */
	public void updateRules(String ruleFileContent) throws IOException {
		RuleFileManager.getInstance().updatePartnerRuleFile(this, ruleFileContent);
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
    	Partner existingURLPartner = FIND.where()
    											.eq("url", url)
    											.not(Expr.eq("id", id))
    											.findUnique();
    	if(existingURLPartner != null) {
    		errors.add(new ValidationError("url", "URL already exists."));
    	}

    	// Ensure source key unique
    	Partner existingKeyPartner = FIND.where()
    											.eq("sourceKey", sourceKey)
    											.not(Expr.eq("id", id))
    											.findUnique();
    	if(existingKeyPartner != null) {
    		errors.add(new ValidationError("sourceKey", "Source key already exists."));
    	}

    	return errors.isEmpty() ? null : errors;
    }

    public String getName() {
    	return name;
    }

    public String getURL() {
    	return url;
    }

    public String getSourceKey() {
    	return sourceKey;
    }

    public List<FederationConfiguration> getFederationConfigurations() {
    	return federationConfigurations;
    }

}
