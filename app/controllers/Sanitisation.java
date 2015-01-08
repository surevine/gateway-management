package controllers;

import java.util.Map;

import models.Project;

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

		String projectKey = postedProperties.get("projectKey")[0];
		if(projectKey == null) {
			return badRequest("projectKey is missing from request");
		}

		String repoSlug = postedProperties.get("repoSlug")[0];
		if(repoSlug == null) {
			return badRequest("repoSlug is missing from request");
		}

		String commitId = postedProperties.get("commitId")[0];
		if(commitId == null) {
			return badRequest("commitId is missing from request");
		}

		MultipartFormData.FilePart postedArchive = body.getFile("archive");
		if(postedArchive == null) {
			return badRequest("Archive is missing from request.");
		}

		String projectSlug = projectKey + "/" + repoSlug;
		Project project = Project.find.where()
										.eq("projectKey", projectKey)
										.eq("repositorySlug", repoSlug)
										.findUnique();
		if(project == null) {
			return notFound("No project configured with slug: " + projectSlug);
		}

		SanitisationResult sanitisationResult;
		try {
			sanitisationResult = GitManagedSanitisationServiceImpl.getInstance().sanitise(postedArchive.getFile(),
																							projectSlug,
																							commitId,
																							project.destinations);
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
