package controllers;

import java.io.IOException;

import javax.inject.Inject;

import models.Destination;

import com.surevine.gateway.auditing.AuditService;
import com.surevine.gateway.auditing.GatewayAction;
import com.surevine.gateway.rules.RuleFileManager;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class GlobalRuleFiles extends Controller {

    @Inject
    private AuditService auditService;

	public static Result edit(String slug) {

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
			flash("load-error", "Could not load global rule file: " + e.getMessage());
		}

		DynamicForm rulesForm = Form.form();

    	return ok(views.html.globalrules.edit.render(slug, ruleFileContents, rulesForm));

	}

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
			flash("error", "Could not update global rules. " + e.getMessage());
			return redirect(routes.GlobalRuleFiles.view());
		}

    	auditService.audit(GatewayAction.MODIFY_GLOBAL_RULES, session().get("username"),
    			String.format("Modified global %s rules", slug));

    	flash("success", String.format("Updated global %s rules.", slug));
    	return redirect(routes.GlobalRuleFiles.view());

	}

	public static Result view() {

		String exportRules = "";
		String importRules = "";

    	try {
    		exportRules = RuleFileManager.getInstance().loadGlobalExportRules();
    	}
    	catch(IOException e) {
    		flash("export-error", "Could not load global export rules: " + e.getMessage());
    	}

    	try {
    		importRules = RuleFileManager.getInstance().loadGlobalImportRules();
    	} catch(IOException e) {
			flash("import-error", "Could not load global import rules: " + e.getMessage());
		}

		return ok(views.html.globalrules.view.render(exportRules, importRules));
	}

}
