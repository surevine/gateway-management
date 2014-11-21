package controllers;

import java.util.List;

import models.Destination;
import models.Project;

import play.libs.Json;
import play.mvc.*;

/**
 * Projects API controller.
 * Offers a sub-set of main projects controller functionality.
 *
 * @author jonnyheavey
 *
 */
@org.springframework.stereotype.Controller
public class ProjectsAPI extends Controller {

	/**
	 * Returns a list of projects that are configured to be shared to 1 or more destination
	 *
	 * @return
	 */
    public Result list() {
    	List<Destination> destinations = Destination.find.all();
    	List<Project> projects = Project.find.where().in("destinations", destinations).findList();
    	return ok(Json.toJson(projects));
    }

    /**
     * Returns JSON representation of single project
     *
     * @param id Id of project to return
     * @return
     */
    public Result view(Long id) {
    	Project project = Project.find.byId(id);
    	if(project == null) {
    		return notFound("Project not found.");
    	}
    	return ok(Json.toJson(project));
    }

}
