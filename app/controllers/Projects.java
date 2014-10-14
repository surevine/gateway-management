package controllers;

import java.io.IOException;
import java.util.List;

import models.Destination;
import models.Project;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Projects extends Controller {

	/**
	 * Display list of all projects
	 *
	 * @return
	 */
	public static Result list() {

		List<Project> projects = Project.find.all();

        return ok(views.html.projects.list.render(projects));

	}

    /**
     * Display single project view page
     *
     * @param id Id of project to display
     * @return
     */
    public static Result view(Long id) {

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
    public static Result add() {

    	Form<Project> projectForm = Form.form(Project.class);

    	return ok(views.html.projects.add.render(projectForm));

    }
    /**
     * Handle the 'new project' form submission
     */
    public static Result create() {

    	Form<Project> projectForm = Form.form(Project.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.projects.add.render(projectForm));
        }

    	projectForm.get().save();

    	// TODO add flash
    	//flash("success", "Destination created successfully.");

    	return redirect(routes.Projects.view(projectForm.get().id));

    }

    /**
     * Display the 'edit project' form page
     *
     * @param id Id of project to edit
     * @return
     */
    public static Result edit(Long id) {

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
    public static Result update(Long id) {

    	Project project = Project.find.byId(id);
    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	Form<Project> projectForm = Form.form(Project.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.projects.edit.render(id, projectForm));
        }

    	projectForm.get().update(id);

    	// TODO add flash
    	//flash("success", "Destination updated successfully.");

    	return redirect(routes.Projects.view(id));

    }

    /**
     * Handles the 'delete project' form submission
     *
     * @param id Id of the project to delete
     */
    public static Result delete(Long id) {

    	Project project = Project.find.byId(id);
    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	project.delete();

    	return redirect(routes.Projects.list());

    }

    /**
     * Display form page to configure destinations to share project with
     * @param projectId Id of project to add destinations to
     * @return
     */
    public static Result addDestinationPage(long projectId) {

    	Project project = Project.find.byId(projectId);

    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	DynamicForm destinationForm = Form.form();

    	return ok(views.html.projects.adddestination.render(project, destinationForm));
    }

    /**
     * Add destination to a project (handles form submission)
     * @param projectId
     * @return
     */
    public static Result addDestination(long projectId) {

    	Project project = Project.find.byId(projectId);

    	if(project == null) {
    		return notFound("Project not found.");
    	}

    	DynamicForm requestData = Form.form().bindFromRequest();

    	long selectedDestinationId = Long.parseLong(requestData.get("destination"));
    	Destination destination = Destination.find.byId(selectedDestinationId);

    	project.addDestination(destination);

    	return redirect(routes.Projects.view(projectId));

    }

}
