package controllers;

import java.util.List;

import models.FederationConfiguration;
import models.Partner;
import models.Repository;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import play.twirl.api.Content;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.CreateRepositoryAction;
import com.surevine.gateway.auditing.action.DeleteRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateRepositoryAction;

public class Repositories extends AuditedController {

	private static final String REPO_NOT_FOUND = "Repository not found.";

	/**
	 * Display list of all repositories
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result list() {
		List<Repository> repos = Repository.FIND.orderBy().asc("id").findList();
        return ok(views.html.repositories.list.render(repos));
	}

	/**
	 * Returns a list of repositories that are configured to be shared to 1 or more partner
	 *
	 * @return JSON encoded list of repos
	 */
    public Result apiList() {
    	List<FederationConfiguration> configs = FederationConfiguration.FIND.where().eq("outboundEnabled", true).findList();
    	List<Repository> repos = Repository.FIND.where().in("federationConfigurations", configs).orderBy().asc("id").findList();
    	return ok(Json.toJson(repos));
    }

    /**
     *
     * @param id Id of project to return
     * @return JSON encoded representation of single repository
     */
    public Result apiView(Long id) {
    	Repository repo = Repository.FIND.byId(id);
    	if(repo == null) {
    		return notFound(REPO_NOT_FOUND);
    	}
    	return ok(Json.toJson(repo));
    }

    /**
     * Display the 'add repository' form page
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result add() {
    	Form<Repository> repoForm = Form.form(Repository.class);
    	return ok(views.html.repositories.add.render(repoForm));
    }

    /**
     * Handle the 'new repository' form submission
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result create() {

    	Form<Repository> repoForm = Form.form(Repository.class).bindFromRequest();

    	if(repoForm.hasErrors()) {
            return badRequest(views.html.repositories.add.render(repoForm));
        }

    	Repository repo = repoForm.get();
    	repo.save();

    	CreateRepositoryAction action = Audit.getCreateRepositoryAction(repo);
    	audit(action);

    	flash("success", "Repository created successfully.");
    	return redirect(routes.Repositories.list());

    }

    /**
     * Display the 'edit repository' form page
     *
     * @param id Id of repo to edit
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result edit(Long id) {

		Repository repo = Repository.FIND.byId(id);

    	if(repo == null) {
    		return notFound(REPO_NOT_FOUND);
    	}

    	Form<Repository> repoForm = Form.form(Repository.class).fill(repo);

    	return ok(views.html.repositories.edit.render(repo.id, repoForm));

    }

    /**
     * Handle the 'update repository' form submission
     *
     * @param id Id of repository to update
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result update(Long id) {

		Repository originalRepo = Repository.FIND.byId(id);
    	if(originalRepo == null) {
    		return notFound(REPO_NOT_FOUND);
    	}

    	Form<Repository> repoForm = Form.form(Repository.class).bindFromRequest();

    	if(repoForm.hasErrors()) {
            return badRequest(views.html.repositories.edit.render(id, repoForm));
        }

    	Repository updatedRepo = repoForm.get();
    	updatedRepo.update(id);

    	UpdateRepositoryAction action = Audit.getUpdateRepositoryAction(originalRepo, updatedRepo);
    	audit(action);

    	flash("success", "Repository updated successfully.");
    	return redirect(routes.Repositories.list());

    }

    /**
     * Handles the 'delete repository' form submission
     *
     * @param id Id of the repository to delete
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result delete(Long id) {

		Repository repo = Repository.FIND.byId(id);
    	if(repo == null) {
    		return notFound(REPO_NOT_FOUND);
    	}

    	repo.delete();

    	DeleteRepositoryAction action = Audit.getDeleteRepositoryAction(repo);
    	audit(action);

    	flash("success", "Repository deleted successfully.");
    	return redirect(routes.Repositories.list());

    }

    /**
     * Display form page to configure sharing repo with partner
     * @param repositoryId Id of repo
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result federateRepoPage(Long repositoryId) {

		Repository repo = Repository.FIND.byId(repositoryId);

    	if(repo == null) {
    		return notFound(REPO_NOT_FOUND);
    	}

    	DynamicForm repoForm = Form.form();

    	return ok(views.html.repositories.federaterepo.render(repo, repoForm));

    }

}
