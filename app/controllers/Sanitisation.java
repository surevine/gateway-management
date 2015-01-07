package controllers;

import java.io.File;

import com.surevine.sanitisation.GitManagedSanitisationServiceImpl;
import com.surevine.sanitisation.SanitisationResult;

import play.Logger;
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

		if(postedArchive == null) {
			return badRequest("Archive is missing from request.");
		}

		File archive = postedArchive.getFile();
		SanitisationResult result = GitManagedSanitisationServiceImpl.getInstance().sanitise(archive);

		if(result.isSane()) {
			return ok();
		}

		return badRequest(result.getOutput());
	}

}
