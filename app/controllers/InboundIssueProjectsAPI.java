package controllers;

import java.util.List;

import models.InboundIssueProject;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Inbound (whitelisted) issue projects API controller.
 * Offers a sub-set of main WhitelistedProjects controller functionality.
 *
 * @author jonnyheavey
 *
 */
public class InboundIssueProjectsAPI extends Controller {

	/**
	 * Returns a list of projects that are whitelisted to be accepted through gateway
	 *
	 * @return
	 */
	public Result list() {
		List<InboundIssueProject> projects = InboundIssueProject.FIND.all();
    	return ok(Json.toJson(projects));
	}

}
