package controllers;

import java.util.List;

import models.Destination;
import play.libs.Json;
import play.mvc.*;

/**
 * Destinations API controller.
 * Offers a sub-set of main destinations controller functionality.
 *
 * @author jonnyheavey
 *
 */
@org.springframework.stereotype.Controller
public class DestinationsAPI extends Controller {

	/**
	 * Display list of all destinations
	 *
	 * @return
	 */
    public Result list() {

    	List<Destination> destinations = Destination.find.all();
    	return ok(Json.toJson(destinations));

    }

}
