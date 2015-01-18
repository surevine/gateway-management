package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Whitelisted SCM project, shared from a remote partner.
 *
 * @author jonnyheavey
 *
 */
@Entity
public class WhitelistedProject extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	/**
	 * Name of organisation project is shared from
	 */
	@Required
	@MaxLength(255)
	public String sourceOrganisation;

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
     * Generic query helper for entity InboundProject with id Long
     */
    public static Model.Finder<Long,WhitelistedProject> find = new Model.Finder<Long,WhitelistedProject>(Long.class, WhitelistedProject.class);


}
