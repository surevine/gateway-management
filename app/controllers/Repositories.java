package controllers;

import java.util.List;

import models.Repository;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.CreateRepositoryAction;
import com.surevine.gateway.auditing.action.DeleteRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateRepositoryAction;

public class Repositories extends AuditedController {

	/**
	 * Display list of all outbound projects
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result list() {
		List<Repository> repos = Repository.FIND.all();
        return ok(views.html.repositories.list.render(repos));
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

    	// TODO audit action

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
    		return notFound("Repository not found.");
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
    		return notFound("Repository not found.");
    	}

    	Form<Repository> repoForm = Form.form(Repository.class).bindFromRequest();

    	if(repoForm.hasErrors()) {
            return badRequest(views.html.repositories.edit.render(id, repoForm));
        }

    	Repository updatedRepo = repoForm.get();
    	updatedRepo.update(id);

    	// TODO audit

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
    		return notFound("Repository not found.");
    	}

    	repo.delete();

    	// TODO audit

    	flash("success", "Repository deleted successfully.");
    	return redirect(routes.Repositories.list());

    }

}
