package projects.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import models.OutboundProject;

import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;
import projects.ProjectTest;

/**
 * Tests for the project controller update functionality (routes/actions)
 *
 * @author jonnyheavey
 *
 */
public class UpdateProjectsControllerTest extends ProjectTest {

	/**
	 * Create existing project in database (for use by update tests)
	 */
	@BeforeClass
	public static void createExistingTestProject() {
		OutboundProject project = new OutboundProject(TEST_EXISTING_PROJECT_ID,
										TEST_EXISTING_PROJECT_DISPLAY_NAME,
										TEST_EXISTING_PROJECT_PROJECT_KEY,
										TEST_EXISTING_PROJECT_SLUG_REPO);
		project.save();
	}

	@Test
	public void testEditProject() {
		FakeRequest request = new FakeRequest(GET, "/projects/edit/" + TEST_EXISTING_PROJECT_ID);
		Result result = callAction(controllers.routes.ref.OutboundProjects.edit(TEST_EXISTING_PROJECT_ID), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testEditNonExistingProject() {
		FakeRequest request = new FakeRequest(GET, "/projects/edit/" + TEST_NON_EXISTING_PROJECT_ID);
		Result result = callAction(controllers.routes.ref.OutboundProjects.edit(TEST_NON_EXISTING_PROJECT_ID), request);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testUpdateProject() {

		Result result = postUpdateProject(TEST_EXISTING_PROJECT_ID,
											TEST_NEW_PROJECT_DISPLAY_NAME,
											TEST_NEW_PROJECT_PROJECT_KEY,
											TEST_NEW_PROJECT_SLUG_REPO);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		OutboundProject project = OutboundProject.find.byId(TEST_EXISTING_PROJECT_ID);
		assertThat(project.displayName).isEqualTo(TEST_NEW_PROJECT_DISPLAY_NAME);
		assertThat(project.projectKey).isEqualTo(TEST_NEW_PROJECT_PROJECT_KEY);
		assertThat(project.repositorySlug).isEqualTo(TEST_NEW_PROJECT_SLUG_REPO);
	}

	@Test
	public void testUpdateProjectEmptyName() {
		Result result = postUpdateProject(TEST_EXISTING_PROJECT_ID,
											"",
											TEST_EXISTING_PROJECT_PROJECT_KEY,
											TEST_EXISTING_PROJECT_SLUG_REPO);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testUpdateProjectEmptyProjectKey() {
		Result result = postUpdateProject(TEST_EXISTING_PROJECT_ID,
												TEST_EXISTING_PROJECT_DISPLAY_NAME,
												"",
												TEST_EXISTING_PROJECT_SLUG_REPO);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testUpdateProjectEmptyRepoSlug() {
		Result result = postUpdateProject(TEST_EXISTING_PROJECT_ID,
											TEST_EXISTING_PROJECT_DISPLAY_NAME,
											TEST_EXISTING_PROJECT_PROJECT_KEY,
											"");

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testUpdateProjectNonExistingID() {
		Result result = postUpdateProject(TEST_NON_EXISTING_PROJECT_ID,
											TEST_EXISTING_PROJECT_DISPLAY_NAME,
											TEST_EXISTING_PROJECT_PROJECT_KEY,
											TEST_EXISTING_PROJECT_SLUG_REPO);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	/**
	 * Helper method for fake posting of form data to update project route
	 * @param id Id of project to update
	 * @param displayName Name of project (fake form field value)
	 * @param projectKey URL slug for project name (fake form field value)
	 * @param repositorySlug URL slug for repo name (fake form field value)
	 * @return Result Response from server
	 */
	private Result postUpdateProject(long id, String displayName, String projectKey, String repositorySlug) {

		Map<String,String> formData = new HashMap<String,String>();
		formData.put("displayName", displayName);
		formData.put("projectKey", projectKey);
		formData.put("repositorySlug", repositorySlug);

		FakeRequest request = new FakeRequest(POST, "/projects/update/" + id);
		Result result = callAction(controllers.routes.ref.OutboundProjects.update(id), request.withFormUrlEncodedBody(formData));

		return result;
	}

}
