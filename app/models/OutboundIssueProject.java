package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
     * Adds destination to share project to
     *
     * @param destination destination to share to
     */
    public void addDestination(Destination destination) {
    	if(!this.destinations.contains(destination)) {
    		this.destinations.add(destination);
    		this.update();

    		// TODO distribute via federator
    	}
    }

}
