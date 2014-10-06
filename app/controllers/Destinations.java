package controllers;

import java.util.List;

import models.Destination;
import play.*;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.*;

import views.html.*;

public class Destinations extends Controller {

	/**
	 * Display list of all destinations
	 *
	 * @return
	 */
    public static Result list() {

    	List<Destination> destinations = Destination.find.all();

        return ok(views.html.destinations.list.render(destinations));

    }

    /**
     * Display single destination view page
     *
     * @param id Id of destination to display
     * @return
     */
    public static Result view(Long id) {

    	Destination destination = Destination.find.byId(id);

    	return ok(views.html.destinations.view.render(destination));

    }

    /**
     * Display the 'add destination' form page
     *
     * @return
     */
    public static Result add() {

    	Form<Destination> destinationForm = Form.form(Destination.class);

    	return ok(views.html.destinations.add.render(destinationForm));

    }

    /**
     * Display the 'edit destination' form page
     *
     * @param id Id of destination to edit
     * @return
     */
    public static Result edit(Long id) {

    	Destination destination = Destination.find.byId(id);
    	Form<Destination> destinationForm = Form.form(Destination.class).fill(destination);

    	return ok(views.html.destinations.edit.render(destination.id, destinationForm));

    }

    /**
     * Handle the 'new destination' form submission
     */
    public static Result create() {

    	Form<Destination> destinationForm = Form.form(Destination.class).bindFromRequest();

    	if(destinationForm.hasErrors()) {
            return badRequest(views.html.destinations.add.render(destinationForm));
        }

    	destinationForm.get().save();

    	// TODO add flash
    	//flash("success", "Destination created successfully.");

    	return redirect(routes.Destinations.view(destinationForm.get().id));

    }

    /**
     * Handle the 'update destination' form submission
     *
     * @param id Id of destination to update
     * @return
     */
    public static Result update(Long id) {

    	Form<Destination> destinationForm = Form.form(Destination.class).bindFromRequest();

    	if(destinationForm.hasErrors()) {
            return badRequest(views.html.destinations.edit.render(id, destinationForm));
        }

    	destinationForm.get().update(id);

    	// TODO add flash
    	//flash("success", "Destination updated successfully.");

    	return redirect(routes.Destinations.view(id));

    }

    /**
     * Handles the 'delete destination' form submission
     *
     * @param id Id of the destination to delete
     */
    public static Result delete(Long id) {

    	Destination destination = Destination.find.byId(id);
    	destination.delete();

    	return redirect(routes.Destinations.list());

    }

}
