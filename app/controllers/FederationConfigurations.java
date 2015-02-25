package controllers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.ResendRepositoryAction;
import com.surevine.gateway.auditing.action.ShareRepositoryAction;
import com.surevine.gateway.auditing.action.UnshareRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateFederationAction;
import com.surevine.gateway.federation.Federator;
import com.surevine.gateway.federation.FederatorServiceException;

import models.Destination;
import models.FederationConfiguration;
import models.Repository;
import models.RepositoryType;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
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
     * Retrieve single federation configuration for repository that has
     * been whitelisted for inbound federation with destination.
     *
     * @return
     */
    public Result singleInboundConfiguration() {

    	Map<String, String[]> queryString = request().queryString();

    	// TODO error checking on querystring values

		Repository repository = Repository.FIND.where()
				.eq("repoType", RepositoryType.valueOf(queryString.get("repoType")[0]))
				.eq("identifier", queryString.get("repoIdentifier")[0])
				.findUnique();

		List<Destination> destinations = Destination.FIND.where().eq("sourceKey", queryString.get("sourceKey")[0]).findList();

		FederationConfiguration inboundConfiguration = FederationConfiguration.FIND.where()
															.eq("inboundEnabled", true)
															.eq("repository", repository)
															.in("destination", destinations).findUnique();

		if(inboundConfiguration != null) {
			return ok(Json.toJson(inboundConfiguration));
		}

		return notFound("Inbound federated repository not found.");

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
	 * Get list of outbound enabled federations configurations
	 * for a given destination
	 *
	 * @param destinationId Id of destination to retrieve federation configurations for
	 * @return
	 */
    public Result outboundForDestination(Long destinationId) {

    	Destination destination = Destination.FIND.byId(destinationId);
    	if(destination == null) {
    		return notFound("Destination not found.");
    	}

    	List<FederationConfiguration> outboundConfigurations = FederationConfiguration.FIND.where()
																.eq("outboundEnabled", true)
																.eq("destination", destination)
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

    	try {
			Federator.distribute(destination, repo);
		} catch (FederatorServiceException e) {
			String errorMessage = String.format("Failed to distribute repository to destination. Distribution will be reattempted at the next configured federation interval.",
									destination.name, repo.identifier);
			Logger.error(errorMessage, e);
			flash("error", errorMessage);
		}

    	ShareRepositoryAction action = Audit.getShareRepositoryAction(config);
    	audit(action);

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

		UpdateFederationAction action = Audit.getUpdateFederationAction(config, requestData.get("direction"), setActive);
		audit(action);

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

		flash("success", "Repository unshared with destination.");

		UnshareRepositoryAction action = Audit.getUnshareRepositoryAction(config);
		audit(action);

		switch(requestData.get("source")) {
			case "destination":
				return redirect(routes.Destinations.list());
			case "repository":
				return redirect(routes.Repositories.list());
			default:
				return badRequest("Unexpected source value.");
		}

	}

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result resend() {

		DynamicForm requestData = Form.form().bindFromRequest();
		Long id = Long.parseLong(requestData.get("configurationId"));

		FederationConfiguration config = FederationConfiguration.FIND.byId(id);
    	if(config == null) {
    		return notFound(String.format("FederationConfiguration with id %s not found.", id));
    	}

    	try {
			Federator.distribute(config.destination, config.repository);
		} catch (FederatorServiceException e) {
			String errorMessage = String.format("Failed to resend repository to destination.",
					config.destination.name, config.repository.identifier);
			Logger.error(errorMessage, e);
			return internalServerError(errorMessage);
		}

    	ResendRepositoryAction action = Audit.getResendRepositoryAction(config);
    	audit(action);

        return ok("Resent repository to gateway for export to destination.");
	}

}
