package controllers;

import java.io.IOException;

import com.surevine.gateway.rules.RuleFileManager;

import play.mvc.Controller;
import play.mvc.Result;

public class GlobalRuleFiles extends Controller {

	public static Result view() {

		boolean loadExportRulesError = false;
		boolean loadImportRulesError = false;

		String exportRules = "";
		String importRules = "";

		String loadExportRulesErrorMessage = "";
		String loadImportRulesErrorMessage = "";

    	try {
    		exportRules = RuleFileManager.getInstance().loadGlobalExportRules();
    	}
    	catch(IOException e) {
    		// Display error to user, but continue render of global rules page
    		loadExportRulesError = true;
    		loadExportRulesErrorMessage = "Could not load global export rules: " + e.getMessage();
    	}

    	try {
    		importRules = RuleFileManager.getInstance().loadGlobalImportRules();
    	} catch(IOException e) {
			// Display error to user, but continue render of global rules page
			loadImportRulesError = true;
			loadImportRulesErrorMessage = "Could not load global import rules: " + e.getMessage();
		}

		return ok(views.html.globalrules.view.render(exportRules,
														loadExportRulesError,
														loadExportRulesErrorMessage,
														importRules,
														loadImportRulesError,
														loadImportRulesErrorMessage));
	}

}
