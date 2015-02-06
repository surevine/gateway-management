package controllers;

import java.io.IOException;
import java.util.List;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.AuditAction;
import com.surevine.gateway.auditing.action.CreateDestinationAction;
import com.surevine.gateway.auditing.action.DeleteDestinationAction;
import com.surevine.gateway.auditing.action.ModifyDestinationRulesAction;
import com.surevine.gateway.auditing.action.UpdateDestinationAction;

import models.Destination;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;

public class Destinations extends AuditedController {

	private static final String DESTINATION_NOT_FOUND = "Destination not found.";

	/**
	 * Display list of all destinations
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
    public Result list() {

    	List<Destination> destinations = Destination.find.all();

        return ok(views.html.destinations.list.render(destinations));

    }

    /**
     * Display single destination view page
     *
     * @param id Id of destination to display
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result view(Long id) {

    	Destination destination = Destination.find.byId(id);

    	if(destination == null) {
    		return notFound();
    	}

    	String destinationRules = "";
    	String errorMessage = "";
    	Boolean error = false;

    	try {
    		destinationRules = destination.loadRules();
    	} catch(IOException e) {
    		// Display error to user, but continue render of destination page
    		error = true;
    		errorMessage = e.getMessage();
    		Logger.error(errorMessage, e);
    	}

    	return ok(views.html.destinations.view.render(destination, destinationRules, error, errorMessage));

    }

    /**
     * Display the 'add destination' form page
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result add() {

    	Form<Destination> destinationForm = Form.form(Destination.class);

    	return ok(views.html.destinations.add.render(destinationForm));

    }

    /**
     * Display the 'edit destination' form page
     *
     * @param id Id of destination to edit
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result edit(Long id) {

    	Destination destination = Destination.find.byId(id);

    	if(destination == null) {
    		return notFound(DESTINATION_NOT_FOUND);
    	}

    	Form<Destination> destinationForm = Form.form(Destination.class).fill(destination);

    	return ok(views.html.destinations.edit.render(destination.id, destinationForm));

    }

    /**
     * Handle the 'new destination' form submission
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result create() {

    	Form<Destination> destinationForm = Form.form(Destination.class).bindFromRequest();

    	if(destinationForm.hasErrors()) {
            return badRequest(views.html.destinations.add.render(destinationForm));
        }

    	Destination destination = destinationForm.get();
    	destination.save();

    	CreateDestinationAction action = Audit.getCreateDestinationAction(destination);
    	audit(action);

    	flash("success", "Created destination.");
    	return redirect(routes.Destinations.view(destinationForm.get().id));

    }

    /**
     * Handle the 'update destination' form submission
     *
     * @param id Id of destination to update
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result update(Long id) {

    	Destination originalDestination = Destination.find.byId(id);
    	if(originalDestination == null) {
    		return notFound(DESTINATION_NOT_FOUND);
    	}

    	Form<Destination> destinationForm = Form.form(Destination.class).bindFromRequest();

    	if(destinationForm.hasErrors()) {
            return badRequest(views.html.destinations.edit.render(id, destinationForm));
        }

    	Destination updatedDestination = destinationForm.get();
    	updatedDestination.update(id);

    	UpdateDestinationAction action = Audit.getUpdateDestinationAction(originalDestination, updatedDestination);
    	audit(action);

    	flash("success", "Updated destination.");
    	return redirect(routes.Destinations.view(id));

    }

    /**
     * Handles the 'delete destination' form submission
     *
     * @param id Id of the destination to delete
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result delete(Long id) {

    	Destination destination = Destination.find.byId(id);
    	if(destination == null) {
    		return notFound(DESTINATION_NOT_FOUND);
    	}

    	destination.delete();

    	DeleteDestinationAction action = Audit.getDeleteDestinationAction(destination);
    	audit(action);

    	flash("success", "Deleted destination.");
    	return redirect(routes.Destinations.list());

    }

    /**
     * Display form page to configure sharing project with destination
     * @param destinationId Id of destination
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result shareProjectPage(Long destinationId) {

    	Destination destination = Destination.find.byId(destinationId);

    	if(destination == null) {
    		return notFound(DESTINATION_NOT_FOUND);
    	}

    	DynamicForm projectForm = Form.form();

    	return ok(views.html.destinations.shareprojects.render(destination, projectForm));

    }

	@Security.Authenticated(AppAuthenticator.class)
    public Result editRules(Long destinationId) {

    	Destination destination = Destination.find.byId(destinationId);

    	if(destination == null) {
    		return notFound(DESTINATION_NOT_FOUND);
    	}

    	String destinationRules;
		try {
    		destinationRules = destination.loadRules();
    	} catch(IOException e) {
    		String errorMessage = "Could not load destination rule file.";
    		Logger.error(errorMessage, e);
    		return notFound(errorMessage);
    	}

		DynamicForm rulesForm = Form.form();

    	return ok(views.html.destinations.editrules.render(destination, destinationRules, rulesForm));

    }

	@Security.Authenticated(AppAuthenticator.class)
    public Result updateRules(Long destinationId) {

    	DynamicForm requestData = Form.form().bindFromRequest();
    	Destination destination = Destination.find.byId(destinationId);
    	if(destination == null) {
    		return notFound(DESTINATION_NOT_FOUND);
    	}

    	String newRuleFileContent = requestData.get("ruleFileContent");

    	try {
			destination.updateRules(newRuleFileContent);
		} catch (IOException e) {
			String errorMessage = "Could not update destination rules. " + e.getMessage();
			Logger.error(errorMessage, e);
			flash("error", errorMessage);
			return redirect(routes.Destinations.view(destinationId));
		}

    	ModifyDestinationRulesAction action = Audit.getModifyDestinationRulesAction(destination, newRuleFileContent);
    	audit(action);

    	flash("success", "Updated destination rules.");
    	return redirect(routes.Destinations.view(destinationId));
    }

}
