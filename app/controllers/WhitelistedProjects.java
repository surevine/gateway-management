package controllers;

import java.util.List;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.CreateRepositoryAction;
import com.surevine.gateway.auditing.action.DeleteRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateRepositoryAction;

import models.Project;
import models.WhitelistedProject;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class WhitelistedProjects extends Controller {

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result list() {
		List<WhitelistedProject> projects = WhitelistedProject.find.all();
        return ok(views.html.whitelist.list.render(projects));
	}

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result add() {
		Form<WhitelistedProject> projectForm = Form.form(WhitelistedProject.class);
		return ok(views.html.whitelist.add.render(projectForm));
	}

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result create() {
    	Form<WhitelistedProject> projectForm = Form.form(WhitelistedProject.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.whitelist.add.render(projectForm));
        }

    	WhitelistedProject project = projectForm.get();
    	project.save();

    	flash("success", "Whitelisted project created successfully.");
    	return redirect(routes.WhitelistedProjects.list());
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result edit(Long id) {

		WhitelistedProject project = WhitelistedProject.find.byId(id);

    	if(project == null) {
    		return notFound("Whitelisted project not found.");
    	}

    	Form<WhitelistedProject> projectForm = Form.form(WhitelistedProject.class).fill(project);

		return ok(views.html.whitelist.edit.render(id, projectForm));
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result update(Long id) {
		WhitelistedProject originalProject = WhitelistedProject.find.byId(id);
    	if(originalProject == null) {
    		return notFound("Whitelisted project not found.");
    	}

    	Form<WhitelistedProject> projectForm = Form.form(WhitelistedProject.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.whitelist.edit.render(id, projectForm));
        }

    	WhitelistedProject updatedProject = projectForm.get();
    	updatedProject.update(id);

    	flash("success", "Whitelisted project updated successfully.");
    	return redirect(routes.WhitelistedProjects.list());
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result delete(Long id) {
		WhitelistedProject project = WhitelistedProject.find.byId(id);
    	if(project == null) {
    		return notFound("Whitelisted project not found.");
    	}

    	project.delete();

    	flash("success", "Whitelisted project deleted successfully.");
    	return redirect(routes.WhitelistedProjects.list());
	}

}
