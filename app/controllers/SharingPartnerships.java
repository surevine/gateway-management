package controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import models.Destination;
import models.Project;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class SharingPartnerships extends Controller {

	/**
	 * Share a source code project with (multiple) destinations
	 * @return
	 */
	public static Result shareProjectWithDestinations() {
		// TODO
		return ok();
	}

	/**
	 * Share (multiple) source code projects with a destination
	 *
	 * @return
	 */
	public static Result shareProjectsWithDestination(Long destinationId) {

		DynamicForm requestData = Form.form().bindFromRequest();

    	long selectedDestinationId = Long.parseLong(requestData.get("destinationId"));
    	Destination destination = Destination.find.byId(selectedDestinationId);

    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

    	// Parse form contents from request
    	Map<String, String[]> requestBody = request().body().asFormUrlEncoded();
    	String[] selectedProjectsArr = requestBody.get("selectedProjects");

    	// Do nothing if no projects selected
    	if(selectedProjectsArr == null) {
    		return redirect(routes.Destinations.view(destination.id));
    	}

    	List<String> selectedProjects = Arrays.asList(selectedProjectsArr);

    	for(String projectId: selectedProjects) {
    		Project project = Project.find.byId(Long.parseLong(projectId));
    		if(project == null) {
    			Logger.warn(String.format("Failed to shared repository with ID [%s] to destination. Repository not found.", projectId));
    			continue;
	    	}
    		destination.addProject(project);
    	}

    	flash("success", "Repositories shared with destination successfully.");
    	return redirect(routes.Destinations.view(destination.id));
	}

	/**
	 * Remove existing sharing partnership
	 *
	 * @return
	 */
	public static Result delete() {

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

    	if(destination.projects.contains(project)) {
        	destination.removeProject(project);
        	flash("success", "Repository unshared with destination successfully.");
        	return redirect(routes.Destinations.view(destination.id));
    	}

    	return notFound("Sharing partnership not found.");

	}

}
