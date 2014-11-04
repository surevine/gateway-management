package controllers;

import java.io.IOException;
import java.util.List;

import com.surevine.gateway.rules.RuleFileManager;

import models.Destination;
import models.Project;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

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

    	String destinationRules = "";
    	String errorMessage = "";
    	Boolean error = false;

    	try {
    		destinationRules = destination.loadRules();
    	}
    	catch(IOException e) {
    		// Display error to user, but continue render of destination page
    		error = true;
    		errorMessage = e.getMessage();
    	}

    	return ok(views.html.destinations.view.render(destination, destinationRules, error, errorMessage));

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

    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

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

    	flash("success", "Created destination.");
    	return redirect(routes.Destinations.view(destinationForm.get().id));

    }

    /**
     * Handle the 'update destination' form submission
     *
     * @param id Id of destination to update
     * @return
     */
    public static Result update(Long id) {

    	Destination destination = Destination.find.byId(id);
    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

    	Form<Destination> destinationForm = Form.form(Destination.class).bindFromRequest();

    	if(destinationForm.hasErrors()) {
            return badRequest(views.html.destinations.edit.render(id, destinationForm));
        }

    	destinationForm.get().update(id);

    	flash("success", "Updated destination.");
    	return redirect(routes.Destinations.view(id));

    }

    /**
     * Handles the 'delete destination' form submission
     *
     * @param id Id of the destination to delete
     */
    public static Result delete(Long id) {

    	Destination destination = Destination.find.byId(id);
    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

    	destination.delete();

    	flash("success", "Deleted destination.");
    	return redirect(routes.Destinations.list());

    }

    /**
     * Display form page to configure sharing project with destination
     * @param destinationId Id of destination
     * @return
     */
    public static Result addProjectPage(Long destinationId) {

    	Destination destination = Destination.find.byId(destinationId);

    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

    	DynamicForm projectForm = Form.form();

    	return ok(views.html.destinations.shareprojects.render(destination, projectForm));

    }

    public static Result editRules(Long destinationId) {

    	Destination destination = Destination.find.byId(destinationId);

    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

    	String destinationRules;
		try {
    		destinationRules = destination.loadRules();
    	}
    	catch(IOException e) {
    		return notFound("Could not load destination rule file.");
    	}

		DynamicForm rulesForm = Form.form();

    	return ok(views.html.destinations.editrules.render(destination, destinationRules, rulesForm));

    }

    public static Result updateRules(Long destinationId) {

    	DynamicForm requestData = Form.form().bindFromRequest();
    	Destination destination = Destination.find.byId(destinationId);
    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

    	String newRuleFileContent = requestData.get("ruleFileContent");

    	try {
			destination.updateRules(newRuleFileContent);
		} catch (IOException e) {
			flash("error", "Could not update destination rules. " + e.getMessage());
			return redirect(routes.Destinations.view(destinationId));
		}

    	flash("success", "Updated destination rules.");
    	return redirect(routes.Destinations.view(destinationId));
    }

}
