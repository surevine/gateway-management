package controllers;

import java.util.List;
import java.util.Map;

import com.avaje.ebean.Expr;
import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.ResendRepositoryAction;
import com.surevine.gateway.auditing.action.ShareRepositoryAction;
import com.surevine.gateway.federation.Federator;
import com.surevine.gateway.federation.FederatorServiceException;
import com.surevine.gateway.scm.service.SCMFederatorServiceException;

import models.Destination;
import models.FederationConfiguration;
import models.OutboundProject;
import models.Repository;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class FederationConfigurations extends AuditedController {

	/**
	 * Display list of all federations
	 *
	 * @return
	 */
    public Result all() {
    	List<FederationConfiguration> configurations = FederationConfiguration.FIND.all();
    	return ok(Json.toJson(configurations));
    }

	/**
	 * Display list of federations configured
	 * for inbound sharing from partners
	 *
	 * @return
	 */
    public Result inbound() {
    	List<FederationConfiguration> inboundConfigurations = FederationConfiguration.FIND.where()
    															.eq("inboundEnabled", true)
    															.findList();
    	return ok(Json.toJson(inboundConfigurations));
    }

	/**
	 * Display list of federations configured
	 * for outbound sharing to partners
	 *
	 * @return
	 */
    public Result outbound() {
    	List<FederationConfiguration> outboundConfigurations = FederationConfiguration.FIND.where()
																.eq("outboundEnabled", true)
																.findList();
    	return ok(Json.toJson(outboundConfigurations));
    }

	/**
	 * Share source code project with a destination (and vice-versa)
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result create() {

		DynamicForm requestData = Form.form().bindFromRequest();
    	Map<String, String[]> requestBody = request().body().asFormUrlEncoded();

    	Destination destination = Destination.FIND.byId(Long.parseLong(requestData.get("destinationId")));
    	if(destination == null) {
    		return notFound(String.format("Destination with id %s not found.", requestData.get("destinationId")));
    	}
    	Repository repo = Repository.FIND.byId(Long.parseLong(requestData.get("repositoryId")));
    	if(repo == null) {
    		return notFound(String.format("Repository with id %s not found.", requestData.get("repositoryId")));
    	}

    	FederationConfiguration existingConfig = FederationConfiguration.FIND.where()
														.eq("destination", destination)
														.eq("repository", repo)
														.findUnique();
    	if(existingConfig != null) {
    		return badRequest(String.format("Federation configuration between destination '%s' and repository '%s' already exists.",
    											destination.name, repo.identifier));
    	}

    	boolean inboundEnabled = false;
    	boolean outboundEnabled = false;

    	switch(requestData.get("federationType")) {
    		case "bidirectional":
    			inboundEnabled = true;
    			outboundEnabled = true;
    			break;
    		case "inbound":
    			inboundEnabled = true;
    			break;
    		case "outbound":
    			outboundEnabled = true;
    			break;
    	}

    	FederationConfiguration config = new FederationConfiguration(destination, repo, inboundEnabled, outboundEnabled);
    	config.save();

    	// TODO maybe no need for the abstract class if only ever used here?
    	try {
			Federator.distribute(destination, repo);
		} catch (FederatorServiceException e) {
			String errorMessage = String.format("Failed to distribute repository to destination.",
									destination.name, repo.identifier);
			Logger.error(errorMessage, e);
			return internalServerError(errorMessage);
		}

    	// TODO audit
//    	ShareRepositoryAction action = Audit.getShareRepositoryAction(project, destination);
//    	audit(action);

    	switch(requestData.get("source")) {
    		case "destination":
    			return redirect(routes.Destinations.list());
    		case "repository":
    			return redirect(routes.Repositories.list());
    		default:
    			return redirect(routes.Destinations.list());
    	}

	}

	@Security.Authenticated(AppAuthenticator.class)
	public Result update() {

		DynamicForm requestData = Form.form().bindFromRequest();
		Long id = Long.parseLong(requestData.get("configurationId"));

		FederationConfiguration config = FederationConfiguration.FIND.byId(id);
		if(config == null) {
			return notFound(String.format("FederationConfiguration with id %s not found.", id));
		}

		// TODO ensure direction and enabled set and valid

		boolean setActive = Boolean.parseBoolean(requestData.get("enable"));

		switch(requestData.get("direction")) {
			case "outbound":
				config.setOutboundEnabled(setActive);
				break;
			case "inbound":
				config.setInboundEnabled(setActive);
				break;
		}

		config.update();

		// TODO audit

		return ok();
	}

	@Security.Authenticated(AppAuthenticator.class)
	public Result delete(Long id) {

		DynamicForm requestData = Form.form().bindFromRequest();

		FederationConfiguration config = FederationConfiguration.FIND.byId(id);
		if(config == null) {
			return notFound(String.format("FederationConfiguration with id %s not found.", id));
		}

		config.delete();

		// TODO audit

		switch(requestData.get("source")) {
			case "destination":
				return redirect(routes.Destinations.list());
			case "repository":
				return redirect(routes.Repositories.list());
			default:
				return redirect(routes.Destinations.list());
		}

	}

	@Security.Authenticated(AppAuthenticator.class)
	public Result resend() {

		DynamicForm requestData = Form.form().bindFromRequest();
		Long id = Long.parseLong(requestData.get("configurationId"));

		FederationConfiguration config = FederationConfiguration.FIND.byId(id);
    	if(config == null) {
    		return notFound(String.format("FederationConfiguration with id %s not found.", id));
    	}

    	// TODO conditionally select federator based on repo type, and resend

    	/*
    	 * Federator.resend(repository, destination)
    	 * Federator inspects repository type, selects federator implementation and sends
    	 */

//    	try {
//        	scmFederator.resend(config.destination.id.toString(), config.repository.identifier);
//    	} catch(SCMFederatorServiceException e) {
//    		String errorMessage = "Failed to resend project to destination.";
//    		Logger.error(errorMessage, e);
//    		return internalServerError(errorMessage);
//    	}

    	// TODO audit
//    	ResendRepositoryAction action = Audit.getResendRepositoryAction(project, destination);
//    	audit(action);

        return ok("Resent repository to gateway for export to destination.");

	}

}
