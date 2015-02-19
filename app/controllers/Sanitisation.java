package controllers;

import java.util.Map;

import models.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.surevine.sanitisation.GitManagedSanitisationServiceImpl;
import com.surevine.sanitisation.SanitisationResult;
import com.surevine.sanitisation.SanitisationServiceException;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;

public class Sanitisation extends Controller {

	/**
	 * Confirms whether posted archive meets sanitisation rules.
	 * @return HTTP 200 if sanitisation executed successfully, else 400.
	 */
	public Result sanitise() {

		MultipartFormData body = request().body().asMultipartFormData();
		Map<String,String[]> postedProperties = body.asFormUrlEncoded();

		String repoIdentifier = postedProperties.get("repoIdentifier")[0];
		if(repoIdentifier == null) {
			return badRequest("identifier is missing from request");
		}

		String repoType = postedProperties.get("repoType")[0];
		if(repoType == null) {
			return badRequest("repoType is missing from request");
		}
		// Sanitisation currently only supports SCM repositories
		// TODO add support for issue tracking repositories
		if(repoType != "SCM") {
			return badRequest("Sanitisation only supports SCM repositories.");
		}

		String identifier = postedProperties.get("identifier")[0];
		if(identifier == null) {
			return badRequest("identifier is missing from request");
		}

		MultipartFormData.FilePart postedArchive = body.getFile("archive");
		if(postedArchive == null) {
			return badRequest("Archive is missing from request.");
		}

		Repository repo = Repository.FIND.where()
											.ieq("repoType", repoType)
											.ieq("identifier", repoIdentifier)
											.findUnique();
		if(repo == null) {
			return notFound("No repository configured with identifier: " + repoIdentifier);
		}

		SanitisationResult sanitisationResult;
		try {
			sanitisationResult = GitManagedSanitisationServiceImpl.getInstance().sanitise(postedArchive.getFile(),
																							repoIdentifier,
																							identifier,
																							repo.getSharedDestinations());
		} catch (SanitisationServiceException e) {
			String errorMessage = "Error executing sanitisation script.";
			Logger.error(errorMessage, e);
			return internalServerError(errorMessage);
		}

		return ok(buildJsonResult(sanitisationResult));

	}

	/**
	 * Creates a JSON representation of a sanitisation result
	 * @param result result to convert to JSON
	 * @return JSON representation of result
	 */
	private ObjectNode buildJsonResult(SanitisationResult result) {
		ObjectNode jsonResult = Json.newObject();
		jsonResult.put("safe", result.isSane());
		JsonNode errors = Json.toJson(result.getErrors());
		jsonResult.put("errors", errors);

		return jsonResult;
	}

}
