package controllers;

import java.util.List;
import java.util.Map;

import models.Repository;
import models.RepositoryType;

import com.surevine.community.sanitisation.SanitisationException;
import com.surevine.community.sanitisation.SanitisationResult;
import com.surevine.sanitisation.SanitisationConfiguration;
import com.surevine.sanitisation.SanitisationService;
import com.surevine.sanitisation.Sanitisation;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;

public class SanitisationAPI extends Controller {

    /**
     * Confirms whether posted archive meets sanitisation rules.
     * @return HTTP 200 if sanitisation executed successfully, else 400.
     */
    public Result sanitise() {

        MultipartFormData body = request().body().asMultipartFormData();
        Map<String,String[]> postedProperties = body.asFormUrlEncoded();

        MultipartFormData.FilePart postedArchive = body.getFile("archive");
        if(postedArchive == null) {
            return badRequest("Archive is missing from request.");
        }

        RepositoryType repoType = RepositoryType.valueOf(postedProperties.get("repoType")[0]);
        if(repoType == null) {
            return badRequest("repoType is missing from request");
        }

        String repoIdentifier = postedProperties.get("repoIdentifier")[0];
        if(repoIdentifier == null) {
            return badRequest("repoIdentifier is missing from request");
        }

        Repository repository = Repository.FIND.where()
                .eq("repoType", repoType)
                .ieq("identifier", repoIdentifier)
                .findUnique();
        if(repository == null) {
            return notFound("No repository configured with identifier: " + repoIdentifier);
        }

        SanitisationResult sanitisationResult;
        try {

            SanitisationService sanitisationService = Sanitisation.getSanitisationServiceFactory().getSanitisationService(repoType);

            // Perform additional implementation specific validation
            List<String> validationErrors = sanitisationService.getValidationErrors(postedProperties);
            if(!validationErrors.isEmpty()) {
                StringBuilder errorMessages = new StringBuilder();
                for(String error : validationErrors) {
                    errorMessages.append(error + System.getProperty("line.separator"));
                }
                return badRequest(validationErrors.toString());
            }

            SanitisationConfiguration sanitisationConfiguration = sanitisationService.buildSanitisationConfiguration(postedArchive.getFile(), postedProperties, repository);
            sanitisationResult = sanitisationService.sanitise(sanitisationConfiguration);

        } catch (SanitisationException e) {
            String errorMessage = "Error executing sanitisation service. " + e.getMessage();
            Logger.error(errorMessage, e);
            return internalServerError(errorMessage);
        }

        return ok(Json.toJson(sanitisationResult));
    }

}
