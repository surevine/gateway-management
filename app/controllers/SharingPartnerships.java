package controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.ResendRepositoryAction;
import com.surevine.gateway.auditing.action.ShareRepositoryAction;
import com.surevine.gateway.auditing.action.UnshareRepositoryAction;
import com.surevine.gateway.federation.FederatorServiceException;
import com.surevine.gateway.federation.issuetracking.IssueTrackingFederatorServiceFacade;
import com.surevine.gateway.scm.service.SCMFederatorServiceException;
import com.surevine.gateway.scm.service.SCMFederatorServiceFacade;

import models.Destination;
import models.OutboundIssueProject;
import models.OutboundProject;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;

public class SharingPartnerships extends AuditedController {

	private static final String DESTINATION_NOT_FOUND = "Destination not found.";
	private static final String PROJECT_NOT_FOUND = "Project not found.";

    /**
     * Service facade for interaction with SCM federator component
     */
    private static SCMFederatorServiceFacade scmFederator = SCMFederatorServiceFacade.getInstance();

    /**
     * Service facade for interation with Issue federator component
     */
    private static IssueTrackingFederatorServiceFacade issueFederator = IssueTrackingFederatorServiceFacade.getInstance();

	/**
	 * Share source code project with a destination (and vice-versa)
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result create() {

		DynamicForm requestData = Form.form().bindFromRequest();
		String source = requestData.get("source");
    	Map<String, String[]> requestBody = request().body().asFormUrlEncoded();

		switch(source) {
			case "destination":
				return createFromDestination(requestData, requestBody);
	    	case "project":
	    		return createFromProject(requestData, requestBody);
	    	case "issueProject":
	    		return createFromIssueProject(requestData, requestBody);
	    	case "destinationIssues":
				return createFromDestinationIssues(requestData, requestBody);
	    	default:
	    		return badRequest("Request source not expected value. Should be either destination or project.");
		}

	}

	/**
	 * Unshare a source code project with a destination (or vice-versa)
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result delete() {

		DynamicForm requestData = Form.form().bindFromRequest();

    	Destination destination = Destination.FIND.byId(Long.parseLong(requestData.get("destinationId")));
    	if(destination == null) {
    		return notFound(DESTINATION_NOT_FOUND);
    	}

    	OutboundProject project = OutboundProject.FIND.byId(Long.parseLong(requestData.get("projectId")));
    	if(project == null) {
    		return notFound(PROJECT_NOT_FOUND);
    	}

    	if(destination.projects.contains(project)) {

    		destination.removeProject(project);

    		UnshareRepositoryAction action = Audit.getUnshareRepositoryAction(project, destination);
        	audit(action);

	    	switch(requestData.get("source")) {
		    	case "destination":
		    		return redirect(routes.Destinations.view(destination.id));
		    	case "project":
		    		return redirect(routes.OutboundProjects.view(project.id));
		    	default:
		    		return badRequest("Request source not expected value. Should be either destination or project.");
	    	}
    	}

    	return notFound("Sharing partnership not found.");
	}

	/**
	 * Unshare a source code project with a destination (or vice-versa)
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result deleteIssue() {

		DynamicForm requestData = Form.form().bindFromRequest();

    	Destination destination = Destination.FIND.byId(Long.parseLong(requestData.get("destinationId")));
    	if(destination == null) {
    		return notFound(DESTINATION_NOT_FOUND);
    	}

    	OutboundIssueProject project = OutboundIssueProject.FIND.byId(Long.parseLong(requestData.get("projectId")));
    	if(project == null) {
    		return notFound(PROJECT_NOT_FOUND);
    	}

    	if(destination.issueProjects.contains(project)) {

    		destination.removeIssueProject(project);

    		// TODO audit

	    	switch(requestData.get("source")) {
		    	case "issueDestination":
		    		return redirect(routes.Destinations.view(destination.id));
		    	case "issueProject":
		    		return redirect(routes.OutboundIssueProjects.view(project.id));
		    	default:
		    		return badRequest("Request source not expected value. Should be either destination or issueProject.");
	    	}
    	}

    	return notFound("Sharing partnership not found.");
	}

	/**
	 * Triggers ad-hoc re-send of repository to destination across gateway
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result resend() {

		DynamicForm requestData = Form.form().bindFromRequest();

    	Destination destination = Destination.FIND.byId(Long.parseLong(requestData.get("destinationId")));
    	if(destination == null) {
    		return notFound(DESTINATION_NOT_FOUND);
    	}

    	OutboundProject project = OutboundProject.FIND.byId(Long.parseLong(requestData.get("projectId")));
    	if(project == null) {
    		return notFound(PROJECT_NOT_FOUND);
    	}

    	if(!destination.projects.contains(project)) {
    		return notFound("Project not shared with destination.");
    	}

    	try {
        	scmFederator.resend(destination.id.toString(), project.projectKey, project.repositorySlug);
    	} catch(SCMFederatorServiceException e) {
    		String errorMessage = "Failed to resend project to destination.";
    		Logger.error(errorMessage, e);
    		return internalServerError(errorMessage);
    	}

    	ResendRepositoryAction action = Audit.getResendRepositoryAction(project, destination);
    	audit(action);

        return ok("Resent project to destination.");
	}

	/**
	 * Triggers ad-hoc re-send of issue repository to destination across gateway
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result resendIssue() {

		DynamicForm requestData = Form.form().bindFromRequest();

    	Destination destination = Destination.FIND.byId(Long.parseLong(requestData.get("destinationId")));
    	if(destination == null) {
    		return notFound(DESTINATION_NOT_FOUND);
    	}

    	OutboundIssueProject project = OutboundIssueProject.FIND.byId(Long.parseLong(requestData.get("projectId")));
    	if(project == null) {
    		return notFound(PROJECT_NOT_FOUND);
    	}

    	if(!destination.issueProjects.contains(project)) {
    		return notFound("Project not shared with destination.");
    	}

    	try {
        	issueFederator.resend(destination.id.toString(), project.projectKey);
    	} catch(FederatorServiceException e) {
    		String errorMessage = "Failed to resend issue repository to destination.";
    		Logger.error(errorMessage, e);
    		return internalServerError(errorMessage);
    	}

    	// TODO audit action

        return ok("Resent project to destination.");
	}

	/**
	 * Shares projects with destination
	 * @param destination Destination to share projects with
	 * @param projectIds array of Project ids to share
	 */
	private void addProjectsToDestination(Destination destination, String[] projectIds) {
		List<String> selectedProjects = Arrays.asList(projectIds);
		List<OutboundProject> projects = OutboundProject.FIND.where().idIn(selectedProjects).findList();

		for(OutboundProject project: projects) {
			destination.addProject(project);
			ShareRepositoryAction action = Audit.getShareRepositoryAction(project, destination);
	    	audit(action);
	    }
	}

	/**
	 * Shares issue projects with destination
	 * @param destination Destination to share projects with
	 * @param projectIds array of Issue Project ids to share
	 */
	private void addIssueProjectsToDestination(Destination destination, String[] projectIds) {
		List<String> selectedProjects = Arrays.asList(projectIds);
		List<OutboundIssueProject> projects = OutboundIssueProject.FIND.where().idIn(selectedProjects).findList();

		for(OutboundIssueProject project: projects) {
			destination.addIssueProject(project);
			// TODO audit action
	    }
	}

	/**
	 * Shares destinations with project
	 * @param project Project to share
	 * @param destinationIds array of Destination ids to share
	 */
	private void addDestinationsToProject(OutboundProject project, String[] destinationIds) {
    	List<String> selectedDestinations = Arrays.asList(destinationIds);
    	List<Destination> destinations = Destination.FIND.where().idIn(selectedDestinations).findList();

    	for(Destination destination: destinations) {
    		project.addDestination(destination);
			ShareRepositoryAction action = Audit.getShareRepositoryAction(project, destination);
	    	audit(action);
    	}
	}

	/**
	 * Shares destinations with project
	 * @param project Project to share
	 * @param destinationIds array of Destination ids to share
	 */
	private void addDestinationsToIssueProject(OutboundIssueProject project, String[] destinationIds) {
    	List<String> selectedDestinations = Arrays.asList(destinationIds);
    	List<Destination> destinations = Destination.FIND.where().idIn(selectedDestinations).findList();

    	for(Destination destination: destinations) {
    		project.addDestination(destination);
    		// TODO audit activity
    	}
	}

	private Result createFromProject(DynamicForm requestData,
			Map<String, String[]> requestBody) {

		OutboundProject project = OutboundProject.FIND.byId(Long.parseLong(requestData.get("projectId")));
		if(project == null) {
			return notFound(PROJECT_NOT_FOUND);
		}

		String[] selectedDestinationsArr = requestBody.get("selectedDestinations");
		if(selectedDestinationsArr == null) {
			return redirect(routes.OutboundProjects.view(project.id));
		}

		addDestinationsToProject(project, selectedDestinationsArr);

		flash("success", "Repository sent to the Gateway to be shared with the destinations.");
		return redirect(routes.OutboundProjects.view(project.id));
	}

	private Result createFromIssueProject(DynamicForm requestData,
			Map<String, String[]> requestBody) {

		OutboundIssueProject project = OutboundIssueProject.FIND.byId(Long.parseLong(requestData.get("projectId")));
		if(project == null) {
			return notFound(PROJECT_NOT_FOUND);
		}

		String[] selectedDestinationsArr = requestBody.get("selectedDestinations");
		if(selectedDestinationsArr == null) {
			return redirect(routes.OutboundIssueProjects.view(project.id));
		}

		addDestinationsToIssueProject(project, selectedDestinationsArr);

		flash("success", "Repository sent to the Gateway to be shared with the destinations.");
		return redirect(routes.OutboundIssueProjects.view(project.id));
	}

	private Result createFromDestination(DynamicForm requestData,
			Map<String, String[]> requestBody) {

		Destination destination = Destination.FIND.byId(Long.parseLong(requestData.get("destinationId")));
		if(destination == null) {
			return notFound(DESTINATION_NOT_FOUND);
		}

		String[] selectedProjectsArr = requestBody.get("selectedProjects");
		if(selectedProjectsArr == null) {
			return redirect(routes.Destinations.view(destination.id));
		}

		addProjectsToDestination(destination, selectedProjectsArr);

		flash("success", "Repositories sent to the Gateway to be shared with destination.");
		return redirect(routes.Destinations.view(destination.id));
	}

	private Result createFromDestinationIssues(DynamicForm requestData,
			Map<String, String[]> requestBody) {

		Destination destination = Destination.FIND.byId(Long.parseLong(requestData.get("destinationId")));
		if(destination == null) {
			return notFound(DESTINATION_NOT_FOUND);
		}

		String[] selectedProjectsArr = requestBody.get("selectedProjects");
		if(selectedProjectsArr == null) {
			return redirect(routes.Destinations.view(destination.id));
		}

		addIssueProjectsToDestination(destination, selectedProjectsArr);

		flash("success", "Repositories sent to the Gateway to be shared with destination.");
		return redirect(routes.Destinations.view(destination.id));
	}

	public static void setSCMFederator(SCMFederatorServiceFacade scmFederator)	{
		SharingPartnerships.scmFederator = scmFederator;
	}

}
