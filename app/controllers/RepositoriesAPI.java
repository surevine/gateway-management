package controllers;

import java.util.List;

import models.Destination;
import models.Repository;

import play.libs.Json;
import play.mvc.*;

/**
 * Projects API controller.
 * Offers a sub-set of main projects controller functionality.
 *
 * @author jonnyheavey
 *
 */
public class RepositoriesAPI extends Controller {

	/**
	 * Returns a list of repositories that are configured to be shared to 1 or more destination
	 *
	 * @return
	 */
    public Result list() {
    	List<Destination> destinations = Destination.FIND.all();
    	List<Repository> repos = Repository.FIND.where().in("destinations", destinations).findList();
    	return ok(Json.toJson(repos));
    }

    /**
     * Returns JSON representation of single repository
     *
     * @param id Id of project to return
     * @return
     */
    public Result view(Long id) {
    	Repository repo = Repository.FIND.byId(id);
    	if(repo == null) {
    		return notFound("Repository not found.");
    	}
    	return ok(Json.toJson(repo));
    }

}
