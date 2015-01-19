package controllers;

import java.util.List;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.CreateRepositoryAction;
import com.surevine.gateway.auditing.action.DeleteRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateRepositoryAction;

import models.OutboundProject;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;

public class OutboundProjects extends AuditedController {

	/**
	 * Display list of all outbound projects
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result list() {

		List<OutboundProject> projects = OutboundProject.find.all();

        return ok(views.html.projects.outbound.list.render(projects));

	}

    /**
     * Display single outbound project view page
     *
     * @param id Id of project to display
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result view(Long id) {

    	OutboundProject project = OutboundProject.find.byId(id);

    	if(project == null) {
    		return notFound("Outbound project not found.");
    	}

    	return ok(views.html.projects.outbound.view.render(project));

    }

    /**
     * Display the 'add outbound project' form page
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result add() {

    	Form<OutboundProject> projectForm = Form.form(OutboundProject.class);

    	return ok(views.html.projects.outbound.add.render(projectForm));

    }

    /**
     * Handle the 'new outbound project' form submission
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result create() {

    	Form<OutboundProject> projectForm = Form.form(OutboundProject.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.projects.outbound.add.render(projectForm));
        }

    	OutboundProject project = projectForm.get();
    	project.save();

    	CreateRepositoryAction action = Audit.getCreateRepositoryAction(project);
    	audit(action);

    	flash("success", "Outbound project created successfully.");
    	return redirect(routes.OutboundProjects.view(projectForm.get().id));

    }

    /**
     * Display the 'edit outbound project' form page
     *
     * @param id Id of project to edit
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result edit(Long id) {

    	OutboundProject project = OutboundProject.find.byId(id);

    	if(project == null) {
    		return notFound("Outbound project not found.");
    	}

    	Form<OutboundProject> projectForm = Form.form(OutboundProject.class).fill(project);

    	return ok(views.html.projects.outbound.edit.render(project.id, projectForm));

    }

    /**
     * Handle the 'update outbound project' form submission
     *
     * @param id Id of project to update
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result update(Long id) {

    	OutboundProject originalProject = OutboundProject.find.byId(id);
    	if(originalProject == null) {
    		return notFound("Outbound project not found.");
    	}

    	Form<OutboundProject> projectForm = Form.form(OutboundProject.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.projects.outbound.edit.render(id, projectForm));
        }

    	OutboundProject updatedProject = projectForm.get();
    	updatedProject.update(id);

    	UpdateRepositoryAction action = Audit.getUpdateRepositoryAction(originalProject, updatedProject);
    	audit(action);

    	flash("success", "Outbound project updated successfully.");
    	return redirect(routes.OutboundProjects.view(id));

    }

    /**
     * Handles the 'delete outbound project' form submission
     *
     * @param id Id of the project to delete
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result delete(Long id) {

    	OutboundProject project = OutboundProject.find.byId(id);
    	if(project == null) {
    		return notFound("Outbound project not found.");
    	}

    	project.delete();

    	DeleteRepositoryAction action = Audit.getDeleteRepositoryAction(project);
    	audit(action);

    	flash("success", "Outbound project deleted successfully.");
    	return redirect(routes.OutboundProjects.list());

    }

    /**
     * Display form page to configure sharing project with destinations
     * @param projectId Id of project
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result shareProjectPage(Long projectId) {

    	OutboundProject project = OutboundProject.find.byId(projectId);

    	if(project == null) {
    		return notFound("Outbound project not found.");
    	}

    	DynamicForm projectForm = Form.form();

    	return ok(views.html.projects.outbound.shareproject.render(project, projectForm));

    }

}
