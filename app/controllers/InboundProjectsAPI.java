package controllers;

import java.util.List;

import models.InboundProject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Inbound (whitelisted) projects API controller.
 * Offers a sub-set of main WhitelistedProjects controller functionality.
 *
 * @author jonnyheavey
 *
 */
public class InboundProjectsAPI extends Controller {

	/**
	 * Returns a list of projects that are whitelisted to be accepted through gateway
	 *
	 * @return
	 */
	public Result list() {
		List<InboundProject> projects = InboundProject.find.all();
    	return ok(Json.toJson(projects));
	}

}
