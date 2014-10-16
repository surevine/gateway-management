package controllers;

import models.Destination;
import models.Project;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class SharingPartnerships extends Controller {

	/**
	 * Create new sharing partnership between destination and project
	 *
	 * @return
	 */
	public static Result create() {

		DynamicForm requestData = Form.form().bindFromRequest();

    	long selectedDestinationId = Long.parseLong(requestData.get("destinationId"));
    	Destination destination = Destination.find.byId(selectedDestinationId);

    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

    	long selectedProjectId = Long.parseLong(requestData.get("projectId"));
    	Project project = Project.find.byId(selectedProjectId);

    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	destination.addProject(project);

    	return redirect(routes.Destinations.view(destination.id));
	}

	/**
	 * Remove existing sharing partnership
	 *
	 * @return
	 */
	public static Result delete() {
		// TODO
		return badRequest();
	}

}
