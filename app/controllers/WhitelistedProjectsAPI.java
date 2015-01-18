package controllers;

import java.util.List;

import models.WhitelistedProject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Whitelisted projects API controller.
 * Offers a sub-set of main WhitelistedProjects controller functionality.
 *
 * @author jonnyheavey
 *
 */
public class WhitelistedProjectsAPI extends Controller {

	/**
	 * Returns a list of projects that are whitelisted to be accepted through gateway
	 *
	 * @return
	 */
	public Result list() {
		List<WhitelistedProject> projects = WhitelistedProject.find.all();
    	return ok(Json.toJson(projects));
	}

}
