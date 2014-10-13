package controllers;

import java.util.List;

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
public class ProjectsAPI extends Controller {

	/**
	 * Display list of all projects
	 *
	 * @return
	 */
    public static Result list() {
    	List<Project> destinations = Project.find.all();
    	return ok(Json.toJson(destinations));
    }

}
