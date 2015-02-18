package controllers;

import java.util.List;
import java.util.Map;

import com.avaje.ebean.Expr;

import models.Destination;
import models.FederationConfiguration;
import models.Repository;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class FederationConfigurations extends Controller {

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

    	// TODO VALIDATE UNIQUE @ MODEL
    	FederationConfiguration config = new FederationConfiguration(destination, repo, inboundEnabled, outboundEnabled);
    	config.save();

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
	public Result update(Long id) {

		DynamicForm requestData = Form.form().bindFromRequest();

		FederationConfiguration config = FederationConfiguration.FIND.byId(id);
		if(config == null) {
			return notFound(String.format("FederationConfiguration with id %s not found.", id));
		}

		// TODO ensure direction and enabled set and valid

		switch(requestData.get("direction")) {
			case "outbound":
				config.setOutboundEnabled(Boolean.parseBoolean(requestData.get("enabled")));
				break;
			case "inbound":
				config.setInboundEnabled(Boolean.parseBoolean(requestData.get("enabled")));
				break;
		}

		config.update();

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

}
