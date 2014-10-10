package controllers;

import java.util.List;

import models.Destination;
import play.libs.Json;
import play.mvc.*;

/**
 * Destinations API controller.
 * Offers a sub-set of main destinations controller,
 * and generally serves JSON responses.
 *
 * @author jonnyheavey
 *
 */
public class DestinationsAPI extends Controller {

	/**
	 * Display list of all destinations
	 *
	 * @return
	 */
    public static Result list() {

    	List<Destination> destinations = Destination.find.all();
    	return ok(Json.toJson(destinations));

    }

}
