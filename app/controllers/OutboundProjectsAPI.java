package controllers;

import java.util.List;

import models.Destination;
import models.OutboundProject;

import play.libs.Json;
import play.mvc.*;

/**
 * Projects API controller.
 * Offers a sub-set of main projects controller functionality.
 *
 * @author jonnyheavey
 *
 */
public class OutboundProjectsAPI extends Controller {

	/**
	 * Returns a list of projects that are configured to be shared to 1 or more destination
	 *
	 * @return
	 */
    public Result list() {
    	List<Destination> destinations = Destination.find.all();
    	List<OutboundProject> projects = OutboundProject.find.where().in("destinations", destinations).findList();
    	return ok(Json.toJson(projects));
    }

    /**
     * Returns JSON representation of single project
     *
     * @param id Id of project to return
     * @return
     */
    public Result view(Long id) {
    	OutboundProject project = OutboundProject.find.byId(id);
    	if(project == null) {
    		return notFound("Project not found.");
    	}
    	return ok(Json.toJson(project));
    }

}
