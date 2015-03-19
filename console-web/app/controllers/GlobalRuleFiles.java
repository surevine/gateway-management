package controllers;

import java.io.IOException;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.ModifyGlobalRulesAction;
import com.surevine.gateway.rules.RuleFileManager;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;

public class GlobalRuleFiles extends AuditedController {

	@Security.Authenticated(AppAuthenticator.class)
	public Result edit(String slug) {

		String ruleFileContents = "";

		try {

			switch(slug) {
			case "export":
				ruleFileContents = RuleFileManager.getInstance().loadGlobalExportRules();
			break;
			case "import":
				ruleFileContents = RuleFileManager.getInstance().loadGlobalImportRules();
			break;
			default:
				return badRequest(String.format("Global rule file slug '%s' not recognised.", slug));
			}

		} catch (IOException e) {
			String errorMessage = "Could not load global rule file: " + e.getMessage();
			Logger.error(errorMessage, e);
			flash("load-error", errorMessage);
		}

		DynamicForm rulesForm = Form.form();

    	return ok(views.html.globalrules.edit.render(slug, ruleFileContents, rulesForm));

	}

	@Security.Authenticated(AppAuthenticator.class)
	public Result update(String slug) {

		DynamicForm requestData = Form.form().bindFromRequest();
		String newRuleFileContent = requestData.get("ruleFileContent");

		try {

			switch(slug) {
			case "export":
				RuleFileManager.getInstance().updateGlobalExportRules(newRuleFileContent);
			break;
			case "import":
				RuleFileManager.getInstance().updateGlobalImportRules(newRuleFileContent);
			break;
			default:
				return badRequest(String.format("Global rule file slug '%s' not recognised.", slug));
			}

		} catch (IOException e) {
			String errorMessage = "Could not update global rules. " + e.getMessage();
			Logger.error(errorMessage, e);
			flash("error", errorMessage);
			return redirect(routes.GlobalRuleFiles.view());
		}

		ModifyGlobalRulesAction action = Audit.getModifyGlobalRulesAction(slug, newRuleFileContent);
    	audit(action);

    	flash("success", String.format("Updated global %s rules.", slug));
    	return redirect(routes.GlobalRuleFiles.view());

	}

	@Security.Authenticated(AppAuthenticator.class)
	public Result view() {

		String exportRules = "";
		String importRules = "";

    	try {
    		exportRules = RuleFileManager.getInstance().loadGlobalExportRules();
    	} catch(IOException e) {
    		String errorMessage = "Could not load global export rules: " + e.getMessage();
    		Logger.error(errorMessage, e);
    		flash("export-error", errorMessage);
    	}

    	try {
    		importRules = RuleFileManager.getInstance().loadGlobalImportRules();
    	} catch(IOException e) {
    		String errorMessage = "Could not load global import rules: " + e.getMessage();
    		Logger.error(errorMessage, e);
			flash("import-error", errorMessage);
		}

		return ok(views.html.globalrules.view.render(exportRules, importRules));
	}

}
