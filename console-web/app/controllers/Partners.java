package controllers;

import java.io.IOException;
import java.util.List;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.CreatePartnerAction;
import com.surevine.gateway.auditing.action.DeletePartnerAction;
import com.surevine.gateway.auditing.action.ModifyPartnerRulesAction;
import com.surevine.gateway.auditing.action.UpdatePartnerAction;

import models.Partner;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;

public class Partners extends AuditedController {

	private static final String PARTNER_NOT_FOUND = "Destination not found.";

	/**
	 * Display list of all partners
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
    public Result list() {
    	List<Partner> partners = Partner.FIND.orderBy().asc("id").findList();
        return ok(views.html.partners.list.render(partners));
    }

	/**
	 * Display list of all partners
	 *
	 * @return JSON encoded partner list
	 */
    public Result apiList() {
    	List<Partner> partners = Partner.FIND.orderBy().asc("id").findList();
    	return ok(Json.toJson(partners));
    }

    /**
     * Display the 'add partner' form page
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result add() {
    	Form<Partner> partnerForm = Form.form(Partner.class);
    	return ok(views.html.partners.add.render(partnerForm));
    }

    /**
     * Display the 'edit partner' form page
     *
     * @param id Id of partner to edit
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result edit(Long id) {

    	Partner partner = Partner.FIND.byId(id);

    	if(partner == null) {
    		return notFound(PARTNER_NOT_FOUND);
    	}

    	Form<Partner> partnerForm = Form.form(Partner.class).fill(partner);
    	return ok(views.html.partners.edit.render(partner.id, partnerForm));
    }

    /**
     * Handle the 'new partner' form submission
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result create() {

    	Form<Partner> partnerForm = Form.form(Partner.class).bindFromRequest();

    	if(partnerForm.hasErrors()) {
            return badRequest(views.html.partners.add.render(partnerForm));
        }

    	Partner partner = partnerForm.get();
    	partner.save();

    	CreatePartnerAction action = Audit.getCreatePartnerAction(partner);
    	audit(action);

    	flash("success", "Created partner.");
    	return redirect(routes.Partners.list());

    }

    /**
     * Handle the 'update partner' form submission
     *
     * @param id Id of partner to update
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result update(Long id) {

    	Partner originalPartner = Partner.FIND.byId(id);
    	if(originalPartner == null) {
    		return notFound(PARTNER_NOT_FOUND);
    	}

    	Form<Partner> partnerForm = Form.form(Partner.class).bindFromRequest();

    	if(partnerForm.hasErrors()) {
            return badRequest(views.html.partners.edit.render(id, partnerForm));
        }

    	Partner updatedPartner = partnerForm.get();
    	updatedPartner.update(id);

    	UpdatePartnerAction action = Audit.getUpdatePartnerAction(originalPartner, updatedPartner);
    	audit(action);

    	flash("success", "Updated partner.");
    	return redirect(routes.Partners.list());

    }

    /**
     * Handles the 'delete partner' form submission
     *
     * @param id Id of the partner to delete
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result delete(Long id) {

    	Partner partner = Partner.FIND.byId(id);
    	if(partner == null) {
    		return notFound(PARTNER_NOT_FOUND);
    	}

    	partner.delete();

    	DeletePartnerAction action = Audit.getDeletePartnerAction(partner);
    	audit(action);

    	flash("success", "Deleted partner.");
    	return redirect(routes.Partners.list());

    }

    /**
     * Display form page to configure sharing project with partner
     * @param partner Id of partner
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result federateRepoPage(Long partnerId) {

    	Partner partner = Partner.FIND.byId(partnerId);

    	if(partner == null) {
    		return notFound(PARTNER_NOT_FOUND);
    	}

    	DynamicForm repoForm = Form.form();

    	return ok(views.html.partners.federaterepo.render(partner, repoForm));

    }

	/**
	 *
	 * @param partnerId
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
    public Result editRules(Long partnerId) {

    	Partner partner = Partner.FIND.byId(partnerId);

    	if(partner == null) {
    		return notFound(PARTNER_NOT_FOUND);
    	}

    	String partnerRules;
		try {
			partnerRules = partner.loadRules();
    	} catch(IOException e) {
    		String errorMessage = "Could not load partner rule file.";
    		Logger.error(errorMessage, e);
    		return notFound(errorMessage);
    	}

		DynamicForm rulesForm = Form.form();

    	return ok(views.html.partners.editrules.render(partner, partnerRules, rulesForm));

    }

	/**
	 *
	 * @param partnerId
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
    public Result updateRules(Long partnerId) {

    	DynamicForm requestData = Form.form().bindFromRequest();
    	Partner partner = Partner.FIND.byId(partnerId);
    	if(partner == null) {
    		return notFound(PARTNER_NOT_FOUND);
    	}

    	String newRuleFileContent = requestData.get("ruleFileContent");

    	try {
    		partner.updateRules(newRuleFileContent);
		} catch (IOException e) {
			String errorMessage = "Could not update partner rules. " + e.getMessage();
			Logger.error(errorMessage, e);
			flash("error", errorMessage);
			return redirect(routes.Partners.list());
		}

    	ModifyPartnerRulesAction action = Audit.getModifyPartnerRulesAction(partner, newRuleFileContent);
    	audit(action);

    	flash("success", "Updated partner rules.");
    	return redirect(routes.Partners.list());
    }

}
