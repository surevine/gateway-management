package controllers;

import java.util.List;
import java.util.Map;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.ResendRepositoryAction;
import com.surevine.gateway.auditing.action.ShareRepositoryAction;
import com.surevine.gateway.auditing.action.UnshareRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateFederationAction;
import com.surevine.gateway.federation.Federator;
import com.surevine.gateway.federation.FederatorServiceException;

import models.Partner;
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

    	Map<String, String[]> queryString = request().queryString();
    	if(queryString.get("repoType") == null) {
    		return badRequest("Missing repoType parameter.");
    	}

    	RepositoryType repoType = RepositoryType.valueOf(queryString.get("repoType")[0]);
		List<Repository> repositories = Repository.FIND.where()
										.eq("repoType", repoType)
										.findList();

    	List<FederationConfiguration> inboundConfigurations = FederationConfiguration.FIND.where()
																.eq("inboundEnabled", true)
																.in("repository", repositories)
																.findList();
    	return ok(Json.toJson(inboundConfigurations));

    }

    /**
     * Retrieve single federation configuration for repository that has
     * been whitelisted for inbound federation with partner.
     *
     * @return
     */
    public Result singleInboundConfiguration() {

    	Map<String, String[]> queryString = request().queryString();

    	if(queryString.get("repoType") == null) {
    		return badRequest("Missing repoType parameter.");
    	}

    	if(queryString.get("repoIdentifier") == null) {
    		return badRequest("Missing repoIdentifier parameter.");
    	}

    	if(queryString.get("sourceKey") == null) {
    		return badRequest("Missing sourceKey parameter.");
    	}

    	RepositoryType repoType = RepositoryType.valueOf(queryString.get("repoType")[0]);
		Repository repository = Repository.FIND.where()
				.eq("repoType", repoType)
				.eq("identifier", queryString.get("repoIdentifier")[0])
				.findUnique();
		if(repository == null) {
			return notFound("Repository not found.");
		}

		List<Partner> partners = Partner.FIND.where().eq("sourceKey", queryString.get("sourceKey")[0]).findList();
		if(partners.isEmpty()) {
			return notFound("No partners found.");
		}

		FederationConfiguration inboundConfiguration = FederationConfiguration.FIND.where()
															.eq("inboundEnabled", true)
															.eq("repository", repository)
															.in("partner", partners).findUnique();

		if(inboundConfiguration != null) {
			return ok(Json.toJson(inboundConfiguration));
		}

		return notFound("Inbound federation configuration for repository/partner not found.");
    }

	/**
	 * Display list of federations configured
	 * for outbound sharing to partners
	 *
	 * @return
	 */
    public Result outbound() {

    	Map<String, String[]> queryString = request().queryString();
    	if(queryString.get("repoType") == null) {
    		return badRequest("Missing repoType parameter.");
    	}

    	RepositoryType repoType = RepositoryType.valueOf(queryString.get("repoType")[0]);
		List<Repository> repositories = Repository.FIND.where()
										.eq("repoType", repoType)
										.findList();

    	List<FederationConfiguration> outboundConfigurations = FederationConfiguration.FIND.where()
																.eq("outboundEnabled", true)
																.in("repository", repositories)
																.findList();
    	return ok(Json.toJson(outboundConfigurations));
    }

	/**
	 * Get list of outbound enabled federations configurations
	 * for a given partner
	 *
	 * @return
	 */
    public Result singleOutboundConfiguration() {

    	Map<String, String[]> queryString = request().queryString();

    	if(queryString.get("repoType") == null) {
    		return badRequest("Missing repoType parameter.");
    	}

    	if(queryString.get("repoIdentifier") == null) {
    		return badRequest("Missing repoIdentifier parameter.");
    	}

    	if(queryString.get("destinationId") == null) {
    		return badRequest("Missing destinationId parameter.");
    	}

    	RepositoryType repoType = RepositoryType.valueOf(queryString.get("repoType")[0]);
		Repository repository = Repository.FIND.where()
				.eq("repoType", repoType)
				.eq("identifier", queryString.get("repoIdentifier")[0])
				.findUnique();
		if(repository == null) {
			return notFound("Repository not found.");
		}

		Long partnerId = Long.parseLong(queryString.get("destinationId")[0]);
		Partner partner = Partner.FIND.byId(partnerId);
		if(partner == null) {
			return notFound("Partner not found.");
		}

    	FederationConfiguration outboundConfiguration = FederationConfiguration.FIND.where()
																.eq("outboundEnabled", true)
																.eq("partner", partner)
																.eq("repository", repository)
																.findUnique();
    	if(outboundConfiguration != null) {
    		return ok(Json.toJson(outboundConfiguration));
    	}

		return notFound("Outbound federation configuration for repository/partner not found.");
    }

	/**
	 * Share source code project with a partner (and vice-versa)
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result create() {

		DynamicForm requestData = Form.form().bindFromRequest();

    	Partner partner = Partner.FIND.byId(Long.parseLong(requestData.get("partnerId")));
    	if(partner == null) {
    		return notFound(String.format("Partner with id %s not found.", requestData.get("partnerId")));
    	}

    	Repository repo = Repository.FIND.byId(Long.parseLong(requestData.get("repositoryId")));
    	if(repo == null) {
    		return notFound(String.format("Repository with id %s not found.", requestData.get("repositoryId")));
    	}

    	FederationConfiguration existingConfig = FederationConfiguration.FIND.where()
														.eq("partner", partner)
														.eq("repository", repo)
														.findUnique();
    	if(existingConfig != null) {
    		return badRequest(String.format("Federation configuration between partner '%s' and repository '%s' already exists.",
    											partner.name, repo.identifier));
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

    	FederationConfiguration config = new FederationConfiguration(partner, repo, inboundEnabled, outboundEnabled);
    	config.save();

    	if(outboundEnabled) {
    		Logger.info("Attempting initial distribution of repository to destination.");
        	try {
    			Federator.distribute(partner, repo);
    		} catch (FederatorServiceException e) {
    			String errorMessage = String.format("Failed to distribute repository to partner. Distribution will be reattempted at the next configured federation interval.",
    									partner.name, repo.identifier);
    			Logger.error(errorMessage, e);
    			flash("error", errorMessage);
    		}
    	} else {
    		Logger.info("Skipping initial distribution as outbound federation disabled.");
    	}

    	ShareRepositoryAction action = Audit.getShareRepositoryAction(config);
    	audit(action);

    	switch(requestData.get("source")) {
    		case "partner":
    			return redirect(routes.Partners.list());
    		case "repository":
    			return redirect(routes.Repositories.list());
    		default:
    			return redirect(routes.Partners.list());
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

		flash("success", "Repository unshared with partner.");

		UnshareRepositoryAction action = Audit.getUnshareRepositoryAction(config);
		audit(action);

		switch(requestData.get("source")) {
			case "partner":
				return redirect(routes.Partners.list());
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
			Federator.distribute(config.partner, config.repository);
		} catch (FederatorServiceException e) {
			String errorMessage = String.format("Failed to resend repository to partner.",
					config.partner.name, config.repository.identifier);
			Logger.error(errorMessage, e);
			return internalServerError(errorMessage);
		}

    	ResendRepositoryAction action = Audit.getResendRepositoryAction(config);
    	audit(action);

        return ok("Resent repository to gateway for export to partner.");
	}

}
