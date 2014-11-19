package controllers;

import java.util.List;

import com.surevine.gateway.auditing.GatewayAction;

import models.Project;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;

public class Projects extends AuditedController {

	/**
	 * Display list of all projects
	 *
	 * @return
	 */
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
    public Result add() {

    	Form<Project> projectForm = Form.form(Project.class);

    	return ok(views.html.projects.add.render(projectForm));

    }
    /**
     * Handle the 'new project' form submission
     */
    public Result create() {

    	Form<Project> projectForm = Form.form(Project.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.projects.add.render(projectForm));
        }

    	Project project = projectForm.get();
    	project.save();

    	audit(GatewayAction.CREATE_REPOSITORY, String.format("Created repository '%s/%s'", project.projectKey, project.repositorySlug));

    	flash("success", "Project created successfully.");
    	return redirect(routes.Projects.view(projectForm.get().id));

    }

    /**
     * Display the 'edit project' form page
     *
     * @param id Id of project to edit
     * @return
     */
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
    public Result update(Long id) {

    	Project project = Project.find.byId(id);
    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	Form<Project> projectForm = Form.form(Project.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.projects.edit.render(id, projectForm));
        }

    	project = projectForm.get();
    	project.update(id);

    	audit(GatewayAction.MODIFY_REPOSITORY, String.format("Modified repository '%s/%s'", project.projectKey, project.repositorySlug));

    	flash("success", "Project updated successfully.");
    	return redirect(routes.Projects.view(id));

    }

    /**
     * Handles the 'delete project' form submission
     *
     * @param id Id of the project to delete
     */
    public Result delete(Long id) {

    	Project project = Project.find.byId(id);
    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	project.delete();

    	audit(GatewayAction.DELETE_REPOSITORY, String.format("Deleted repository '%s/%s'", project.projectKey, project.repositorySlug));

    	flash("success", "Project deleted successfully.");
    	return redirect(routes.Projects.list());

    }

    /**
     * Display form page to configure sharing project with destinations
     * @param projectId Id of project
     * @return
     */
    public Result shareProjectPage(Long projectId) {

    	Project project = Project.find.byId(projectId);

    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	DynamicForm projectForm = Form.form();

    	return ok(views.html.projects.shareproject.render(project, projectForm));

    }

}
