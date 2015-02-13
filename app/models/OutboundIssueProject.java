package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.surevine.gateway.federation.FederatorServiceException;
import com.surevine.gateway.federation.issuetracking.IssueTrackingFederatorServiceFacade;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Issue project in Issue-tracking system to be shared across Gateway.
 * @author jonnyheavey
 */
@Entity
public class OutboundIssueProject extends Model {

    private static final long serialVersionUID = 1L;

    /**
     * Management console ID of project. Internal (console) use only.
     */
	@Id
	public Long id;

	/**
	 * Human readable name for display in console UI
	 */
	@Required
	@MaxLength(255)
	public String displayName;

	/**
	 * Project key as configured in issue-tracking system
	 */
	@Required
	@MaxLength(255)
	@Column(unique=true)
	public String projectKey;

	/**
	 * Destinations the project is configured to be shared with
	 */
	@ManyToMany(mappedBy = "issueProjects", cascade=CascadeType.ALL)
	@JsonBackReference
	public List<Destination> destinations = new ArrayList<Destination>();

    /**
     * Generic query helper for entity OutboundIssueProject with id Long
     */
    public static final Model.Finder<Long,OutboundIssueProject> FIND = new Model.Finder<Long,OutboundIssueProject>(Long.class, OutboundIssueProject.class);

    public OutboundIssueProject(String displayName, String projectKey) {
    	this.displayName = displayName;
    	this.projectKey = projectKey;
    }

    /**
     * List of all projects, used by scala helper in templates (to populate select options)
     * @return Map<String, String> option key/values
     */
    public static List<OutboundIssueProject> allProjectShareOptions(Destination destination) {
    	List<OutboundIssueProject> unsharedProjects = new ArrayList<OutboundIssueProject>();

    	List<OutboundIssueProject> allProjects = FIND.all();

    	for(OutboundIssueProject project : allProjects) {
    		if(!destination.issueProjects.contains(project)) {
    			unsharedProjects.add(project);
    		}
    	}

    	return unsharedProjects;
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

    		try {
				IssueTrackingFederatorServiceFacade.getInstance().distribute(destination.id.toString(), this.projectKey);
			} catch (FederatorServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}
    }

}
