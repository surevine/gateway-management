package controllers;

import java.util.List;

import models.InboundIssueProject;
import models.InboundProject;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class InboundIssueProjects extends Controller {

	/**
	 * Display list of all inbound issue projects
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result list() {
		List<InboundIssueProject> projects = InboundIssueProject.FIND.all();
        return ok(views.html.issueprojects.inbound.list.render(projects));
	}

    /**
     * Display the 'add inbound issue project' form page
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
	public Result add() {
		Form<InboundIssueProject> projectForm = Form.form(InboundIssueProject.class);
		return ok(views.html.issueprojects.inbound.add.render(projectForm));
	}

    /**
     * Handle the 'new issue project' form submission
     *
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
	public Result create() {
    	Form<InboundIssueProject> projectForm = Form.form(InboundIssueProject.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.issueprojects.inbound.add.render(projectForm));
        }

    	InboundIssueProject project = projectForm.get();
    	project.save();

    	flash("success", "Inbound issue project created successfully.");
    	return redirect(routes.InboundIssueProjects.list());
	}

    /**
     * Display the 'edit inbound issue project' form page
     *
     * @param id Id of project to edit
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
	public Result edit(Long id) {

		InboundIssueProject project = InboundIssueProject.FIND.byId(id);

    	if(project == null) {
    		return notFound("Inbound issue project not found.");
    	}

    	Form<InboundIssueProject> projectForm = Form.form(InboundIssueProject.class).fill(project);

		return ok(views.html.issueprojects.inbound.edit.render(id, projectForm));
	}

    /**
     * Handle the 'update inbound issue project' form submission
     *
     * @param id Id of project to update
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
	public Result update(Long id) {
		InboundIssueProject originalProject = InboundIssueProject.FIND.byId(id);
    	if(originalProject == null) {
    		return notFound("Inbound issue project not found.");
    	}

    	Form<InboundIssueProject> projectForm = Form.form(InboundIssueProject.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.issueprojects.inbound.edit.render(id, projectForm));
        }

    	InboundIssueProject updatedProject = projectForm.get();
    	updatedProject.update(id);

    	flash("success", "Inbound issue project updated successfully.");
    	return redirect(routes.InboundIssueProjects.list());
	}

    /**
     * Handles the 'delete inbound issue project' form submission
     *
     * @param id Id of the project to delete
     */
	@Security.Authenticated(AppAuthenticator.class)
	public Result delete(Long id) {
		InboundIssueProject project = InboundIssueProject.FIND.byId(id);
    	if(project == null) {
    		return notFound("Inbound issue project not found.");
    	}

    	project.delete();

    	flash("success", "Inbound issue project deleted successfully.");
    	return redirect(routes.InboundIssueProjects.list());
	}

}
