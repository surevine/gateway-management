package controllers;

import java.io.File;

import org.eclipse.jgit.api.errors.GitAPIException;

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
	 * @return 200 if archive passed sanitisation, else 400.
	 */
	public Result sanitise() {

		MultipartFormData body = request().body().asMultipartFormData();
		MultipartFormData.FilePart postedArchive = body.getFile("archive");

		// TODO extend to ensure that other properties have also been posted
		if(postedArchive == null) {
			return badRequest("Archive is missing from request.");
		}

		File archive = postedArchive.getFile();
		SanitisationResult sanitisationResult;
		try {
			sanitisationResult = GitManagedSanitisationServiceImpl.getInstance().sanitise(archive);
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
	 * @return
	 */
	private ObjectNode buildJsonResult(SanitisationResult result) {
		ObjectNode jsonResult = Json.newObject();
		jsonResult.put("safe", result.isSane());
		jsonResult.put("message", result.getOutput());
		return jsonResult;
	}

}
