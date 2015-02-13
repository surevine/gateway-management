package controllers;

import java.util.List;

import models.OutboundIssueProject;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;

public class OutboundIssueProjects extends AuditedController {

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result list() {
		List<OutboundIssueProject> issueprojects = OutboundIssueProject.FIND.all();
        return ok(views.html.issueprojects.outbound.list.render(issueprojects));
	}

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result view(Long id) {
		OutboundIssueProject project = OutboundIssueProject.FIND.byId(id);
    	if(project == null) {
    		return notFound("Outbound issue project not found.");
    	}
    	return ok(views.html.issueprojects.outbound.view.render(project));
	}

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result add() {
    	Form<OutboundIssueProject> projectForm = Form.form(OutboundIssueProject.class);
    	return ok(views.html.issueprojects.outbound.add.render(projectForm));
	}

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result create() {
    	Form<OutboundIssueProject> projectForm = Form.form(OutboundIssueProject.class).bindFromRequest();
    	if(projectForm.hasErrors()) {
            return badRequest(views.html.issueprojects.outbound.add.render(projectForm));
        }
    	OutboundIssueProject project = projectForm.get();
    	project.save();

    	// TODO add audit action

    	flash("success", "Outbound issue project created successfully.");
    	return redirect(routes.OutboundIssueProjects.view(projectForm.get().id));
	}

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result edit(Long id) {
		OutboundIssueProject project = OutboundIssueProject.FIND.byId(id);

    	if(project == null) {
    		return notFound("Outbound issue project not found.");
    	}

    	Form<OutboundIssueProject> projectForm = Form.form(OutboundIssueProject.class).fill(project);

    	return ok(views.html.issueprojects.outbound.edit.render(project.id, projectForm));
	}

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result update(Long id) {

		OutboundIssueProject originalProject = OutboundIssueProject.FIND.byId(id);
    	if(originalProject == null) {
    		return notFound("Outbound issue project not found.");
    	}

    	Form<OutboundIssueProject> projectForm = Form.form(OutboundIssueProject.class).bindFromRequest();

    	if(projectForm.hasErrors()) {
            return badRequest(views.html.issueprojects.outbound.edit.render(id, projectForm));
        }

    	OutboundIssueProject updatedProject = projectForm.get();
    	updatedProject.update(id);

    	// TODO audit action

    	flash("success", "Outbound issue project updated successfully.");
    	return redirect(routes.OutboundIssueProjects.view(id));
	}

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(AppAuthenticator.class)
	public Result delete(Long id) {

		OutboundIssueProject project = OutboundIssueProject.FIND.byId(id);
    	if(project == null) {
    		return notFound("Outbound issue project not found.");
    	}

    	project.delete();

    	// TODO audit action

    	flash("success", "Outbound issue project deleted successfully.");
    	return redirect(routes.OutboundIssueProjects.list());
	}

    /**
     * Display form page to configure sharing issue project with destinations
     * @param projectId Id of project
     * @return
     */
	@Security.Authenticated(AppAuthenticator.class)
    public Result shareProjectPage(Long projectId) {

    	OutboundIssueProject project = OutboundIssueProject.FIND.byId(projectId);

    	if(project == null) {
    		return notFound("Outbound issue project not found.");
    	}

    	DynamicForm projectForm = Form.form();

    	return ok(views.html.issueprojects.outbound.shareproject.render(project, projectForm));

    }

}
