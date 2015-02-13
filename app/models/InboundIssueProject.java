package models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Expr;

import play.data.validation.ValidationError;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Inbound (whitelisted) issue project, shared from a remote partner.
 *
 * @author jonnyheavey
 *
 */
@Entity
public class InboundIssueProject extends Model {

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
	 * Project key as configured in issue-tracking system
	 */
	@Required
	@MaxLength(255)
	public String projectKey;

    /**
     * Generic query helper for entity InboundIssueProject with id Long
     */
    public static final Model.Finder<Long,InboundIssueProject> FIND = new Model.Finder<Long,InboundIssueProject>(Long.class, InboundIssueProject.class);

    /**
     * Perform additional validation (beyond annotations) on model properties.
     *
     * @return List<ValidationError> validation error message
     */
    public List<ValidationError> validate() {

    	List<ValidationError> errors = new ArrayList<ValidationError>();

    	InboundIssueProject existingProject = FIND.where()
    									.ieq("sourceOrganisation", sourceOrganisation)
    									.ieq("projectKey", projectKey)
    									.not(Expr.eq("id", id))
    									.findUnique();

    	if(existingProject != null) {
    		errors.add(new ValidationError("", "Inbound issue project already exists."));
    	}

    	Pattern p = Pattern.compile("([^-_a-zA-Z0-9])");
    	Matcher projectKeyMatcher = p.matcher(projectKey);
        if (projectKeyMatcher.find()) {
        	errors.add(new ValidationError("projectKey", "Project key contains invalid characters."));
        }

    	return errors.isEmpty() ? null : errors;
    }

}
