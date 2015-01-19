package controllers;

import java.util.List;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.CreateRepositoryAction;
import com.surevine.gateway.auditing.action.DeleteRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateRepositoryAction;

import models.OutboundProject;
import models.InboundProject;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class InboundProjects extends Controller {

	/**
	 * Display list of all inbound projects
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result list() {
		List<InboundProject> projects = InboundProject.find.all();
        return ok(views.html.projects.inbound.list.render(projects));
	}

    /**
     * Display the 'add inbound project' form page
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
	public Result add() {
		Form<InboundProject> projectForm = Form.form(InboundProject.class);
		return ok(views.html.projects.inbound.add.render(projectForm));
	}

    /**
     * Handle the 'new project' form submission
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
	public Result create() {
    	Form<InboundProject> projectForm = Form.form(InboundProject.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.projects.inbound.add.render(projectForm));
        }

    	InboundProject project = projectForm.get();
    	project.save();

    	flash("success", "Inbound project created successfully.");
    	return redirect(routes.InboundProjects.list());
	}

    /**
     * Display the 'edit inbound project' form page
     *
     * @param id Id of project to edit
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
	public Result edit(Long id) {

		InboundProject project = InboundProject.find.byId(id);

    	if(project == null) {
    		return notFound("Inbound project not found.");
    	}

    	Form<InboundProject> projectForm = Form.form(InboundProject.class).fill(project);

		return ok(views.html.projects.inbound.edit.render(id, projectForm));
	}

    /**
     * Handle the 'update inbound project' form submission
     *
     * @param id Id of project to update
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
	public Result update(Long id) {
		InboundProject originalProject = InboundProject.find.byId(id);
    	if(originalProject == null) {
    		return notFound("Inbound project not found.");
    	}

    	Form<InboundProject> projectForm = Form.form(InboundProject.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.projects.inbound.edit.render(id, projectForm));
        }

    	InboundProject updatedProject = projectForm.get();
    	updatedProject.update(id);

    	flash("success", "Inbound project updated successfully.");
    	return redirect(routes.InboundProjects.list());
	}

    /**
     * Handles the 'delete inbound project' form submission
     *
     * @param id Id of the project to delete
     */
	@Security.Authenticated(AppAuthenticator.class)
	public Result delete(Long id) {
		InboundProject project = InboundProject.find.byId(id);
    	if(project == null) {
    		return notFound("Inbound project not found.");
    	}

    	project.delete();

    	flash("success", "Inbound project deleted successfully.");
    	return redirect(routes.InboundProjects.list());
	}

}
