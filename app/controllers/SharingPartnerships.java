package controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.surevine.gateway.auditing.AuditService;
import com.surevine.gateway.auditing.GatewayAction;
import com.surevine.gateway.auditing.LogfileAuditServiceImpl;
import com.surevine.gateway.scm.service.SCMFederatorServiceException;
import com.surevine.gateway.scm.service.SCMFederatorServiceFacade;

import models.Destination;
import models.Project;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class SharingPartnerships extends Controller {

    /**
     * Service facade for interaction with SCM federator component
     */
    private SCMFederatorServiceFacade scmFederator = SCMFederatorServiceFacade.getInstance();

    @Inject
    private AuditService auditService;

	/**
	 * Share source code project with a destination (and vice-versa)
	 *
	 * @return
	 */
	public Result create() {

		DynamicForm requestData = Form.form().bindFromRequest();
		String source = requestData.get("source");
    	Map<String, String[]> requestBody = request().body().asFormUrlEncoded();

		switch(source) {
			case "destination":

		    	long selectedDestinationId = Long.parseLong(requestData.get("destinationId"));
		    	Destination destination = Destination.find.byId(selectedDestinationId);
		    	if(destination == null) {
		    		return notFound("Destination not found.");
		    	}

		    	String[] selectedProjectsArr = requestBody.get("selectedProjects");
		    	if(selectedProjectsArr == null) {
		    		return redirect(routes.Destinations.view(destination.id));
		    	}

		    	addProjectsToDestination(destination, selectedProjectsArr);

		    	flash("success", "Repositories shared with destination successfully.");
		    	return redirect(routes.Destinations.view(destination.id));

	    	case "project":

	    		long selectedProjectId = Long.parseLong(requestData.get("projectId"));
	        	Project project = Project.find.byId(selectedProjectId);
	        	if(project == null) {
	        		return notFound("Project not found.");
	        	}

	        	String[] selectedDestinationsArr = requestBody.get("selectedDestinations");
	        	if(selectedDestinationsArr == null) {
	        		return redirect(routes.Projects.view(project.id));
	        	}

	        	addDestinationsToProject(project, selectedDestinationsArr);

	        	flash("success", "Repository shared with destinations successfully.");
	        	return redirect(routes.Projects.view(project.id));

	    	default:
	    		return badRequest("Request source not expected value. Should be either destination or project.");
		}

	}

	/**
	 * Unshare a source code project with a destination (or vice-versa)
	 *
	 * @return
	 */
	public Result delete() {

		DynamicForm requestData = Form.form().bindFromRequest();

    	long selectedDestinationId = Long.parseLong(requestData.get("destinationId"));
    	long selectedProjectId = Long.parseLong(requestData.get("projectId"));
    	String source = requestData.get("source");

    	Destination destination = Destination.find.byId(selectedDestinationId);
    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

    	Project project = Project.find.byId(selectedProjectId);
    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	if(destination.projects.contains(project)) {

    		destination.removeProject(project);

    		auditService.audit(GatewayAction.UNSHARE_REPOSITORY, session().get("username"),
    							String.format("Unshared repository %s with destination %s", project.displayName, destination.name));

	    	switch(source) {
		    	case "destination":
		    		return redirect(routes.Destinations.view(destination.id));

		    	case "project":
		    		return redirect(routes.Projects.view(project.id));

		    	default:
		    		return badRequest("Request source not expected value. Should be either destination or project.");
	    	}
    	}

    	return notFound("Sharing partnership not found.");
	}

	/**
	 * Triggers ad-hoc re-send of repository to destination across gateway
	 * @return
	 */
	public Result resend() {

		DynamicForm requestData = Form.form().bindFromRequest();

    	long destinationId = Long.parseLong(requestData.get("destinationId"));
    	long projectId = Long.parseLong(requestData.get("projectId"));

    	Destination destination = Destination.find.byId(destinationId);
    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

    	Project project = Project.find.byId(projectId);
    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	if(!destination.projects.contains(project)) {
    		return notFound("Project not shared with destination.");
    	}

    	try {
        	scmFederator.resend(destination.id.toString(), project.projectKey, project.repositorySlug);
    	} catch(SCMFederatorServiceException e) {
    		return internalServerError("Failed to resend project to destination.");
    	}

    	auditService.audit(GatewayAction.RESEND_REPOSITORY, session().get("username"),
    			String.format("Manually resent shared repository %s with destination %s", project.displayName, destination.name));

        return ok("Resent project to destination.");
	}

	/**
	 * Shares projects with destination
	 * @param destination Destination to share projects with
	 * @param projectIds array of Project ids to share
	 */
	private void addProjectsToDestination(Destination destination, String[] projectIds) {
		List<String> selectedProjects = Arrays.asList(projectIds);
		List<Project> projects = Project.find.where().idIn(selectedProjects).findList();

		for(Project project: projects) {
			destination.addProject(project);
    		auditService.audit(GatewayAction.SHARE_REPOSITORY, session().get("username"),
					String.format("Shared repository %s with destination %s", project.displayName, destination.name));
		}
	}

	/**
	 * Shares destinations with project
	 * @param project Project to share
	 * @param destinationIds array of Destination ids to share
	 */
	private void addDestinationsToProject(Project project, String[] destinationIds) {
    	List<String> selectedDestinations = Arrays.asList(destinationIds);
    	List<Destination> destinations = Destination.find.where().idIn(selectedDestinations).findList();

    	for(Destination destination: destinations) {
    		project.addDestination(destination);
    		auditService.audit(GatewayAction.SHARE_REPOSITORY, session().get("username"),
					String.format("Shared repository %s with destination %s", project.displayName, destination.name));
    	}
	}

}
