package controllers;

import java.util.List;

import com.surevine.gateway.auditing.Audit;
import com.surevine.gateway.auditing.action.CreateRepositoryAction;
import com.surevine.gateway.auditing.action.DeleteRepositoryAction;
import com.surevine.gateway.auditing.action.UpdateRepositoryAction;

import models.Project;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;

public class Projects extends AuditedController {

	/**
	 * Display list of all projects
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result list() {

		List<Project> projects = Project.find.all();

        return ok(views.html.projects.list.render(projects));

	}

    /**
     * Display single project view page
     *
     * @param id Id of project to display
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result view(Long id) {

    	Project project = Project.find.byId(id);

    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	return ok(views.html.projects.view.render(project));

    }

    /**
     * Display the 'add project' form page
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result add() {

    	Form<Project> projectForm = Form.form(Project.class);

    	return ok(views.html.projects.add.render(projectForm));

    }
    /**
     * Handle the 'new project' form submission
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result create() {

    	Form<Project> projectForm = Form.form(Project.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.projects.add.render(projectForm));
        }

    	Project project = projectForm.get();
    	project.save();

    	CreateRepositoryAction action = Audit.getCreateRepositoryAction(project);
    	audit(action);

    	flash("success", "Project created successfully.");
    	return redirect(routes.Projects.view(projectForm.get().id));

    }

    /**
     * Display the 'edit project' form page
     *
     * @param id Id of project to edit
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result edit(Long id) {

    	Project project = Project.find.byId(id);

    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	Form<Project> projectForm = Form.form(Project.class).fill(project);

    	return ok(views.html.projects.edit.render(project.id, projectForm));

    }

    /**
     * Handle the 'update project' form submission
     *
     * @param id Id of project to update
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result update(Long id) {

    	Project originalProject = Project.find.byId(id);
    	if(originalProject == null) {
    		return notFound("Project not found.");
    	}

    	Form<Project> projectForm = Form.form(Project.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.projects.edit.render(id, projectForm));
        }

    	Project updatedProject = projectForm.get();
    	updatedProject.update(id);

    	UpdateRepositoryAction action = Audit.getUpdateRepositoryAction(originalProject, updatedProject);
    	audit(action);

    	flash("success", "Project updated successfully.");
    	return redirect(routes.Projects.view(id));

    }

    /**
     * Handles the 'delete project' form submission
     *
     * @param id Id of the project to delete
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result delete(Long id) {

    	Project project = Project.find.byId(id);
    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	project.delete();

    	DeleteRepositoryAction action = Audit.getDeleteRepositoryAction(project);
    	audit(action);

    	flash("success", "Project deleted successfully.");
    	return redirect(routes.Projects.list());

    }

    /**
     * Display form page to configure sharing project with destinations
     * @param projectId Id of project
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result shareProjectPage(Long projectId) {

    	Project project = Project.find.byId(projectId);

    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	DynamicForm projectForm = Form.form();

    	return ok(views.html.projects.shareproject.render(project, projectForm));

    }

}
