package controllers;

import java.util.List;

import models.Destination;
import models.OutboundIssueProject;

import play.libs.Json;
import play.mvc.*;

/**
 * IssueProjects API controller.
 * Offers a sub-set of main projects controller functionality.
 *
 * @author jonnyheavey
 *
 */
public class OutboundIssueProjectsAPI extends Controller {

	/**
	 * Returns a list of projects that are configured to be shared to 1 or more destination
	 *
	 * @return
	 */
    public Result list() {
    	List<Destination> destinations = Destination.FIND.all();
    	List<OutboundIssueProject> projects = OutboundIssueProject.FIND.where().in("destinations", destinations).findList();
    	return ok(Json.toJson(projects));
    }

    /**
     * Returns JSON representation of single project
     *
     * @param id Id of project to return
     * @return
     */
    public Result view(Long id) {
    	OutboundIssueProject project = OutboundIssueProject.FIND.byId(id);
    	if(project == null) {
    		return notFound("Project not found.");
    	}
    	return ok(Json.toJson(project));
    }

}
