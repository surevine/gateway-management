package projects.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import models.Project;

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

	private static final String TEST_NEW_PROJECT_URL = "ssh://user@host.xz:port/path/to/project-a-updated.git/";
	private static final String TEST_NEW_PROJECT_NAME = "Project A Updated";

	/**
	 * Create existing project in database (for use by update tests)
	 */
	@BeforeClass
	public static void createExistingTestProject() {
		Project project = new Project(TEST_EXISTING_PROJECT_ID, TEST_EXISTING_PROJECT_NAME, TEST_EXISTING_PROJECT_URL);
		project.save();
	}

	@Test
	public void testEditProject() {
		FakeRequest request = new FakeRequest(GET, "/projects/edit/" + TEST_EXISTING_PROJECT_ID);
		Result result = callAction(controllers.routes.ref.Projects.edit(TEST_EXISTING_PROJECT_ID), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testEditNonExistingProject() {
		FakeRequest request = new FakeRequest(GET, "/projects/edit/" + TEST_NON_EXISTING_PROJECT_ID);
		Result result = callAction(controllers.routes.ref.Projects.edit(TEST_NON_EXISTING_PROJECT_ID), request);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testUpdateProject() {

		Result result = postUpdateProject(TEST_EXISTING_PROJECT_ID, TEST_NEW_PROJECT_NAME, TEST_NEW_PROJECT_URL);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		Project project = Project.find.byId(TEST_EXISTING_PROJECT_ID);
		assertThat(project.name).isEqualTo(TEST_NEW_PROJECT_NAME);
		assertThat(project.url).isEqualTo(TEST_NEW_PROJECT_URL);
	}

	public void testUpdateProjectEmptyName() {
		Result result = postUpdateProject(TEST_EXISTING_PROJECT_ID, "", TEST_EXISTING_PROJECT_URL);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	public void testUpdateProjectEmptyURL() {
		Result result = postUpdateProject(TEST_EXISTING_PROJECT_ID, TEST_EXISTING_PROJECT_NAME, "");

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testUpdateProjectInvalidURL() {
		Result result = postUpdateProject(TEST_EXISTING_PROJECT_ID, TEST_EXISTING_PROJECT_NAME, TEST_INVALID_URL);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testUpdateProjectNonExistingID() {
		Result result = postUpdateProject(TEST_NON_EXISTING_PROJECT_ID, TEST_EXISTING_PROJECT_NAME, TEST_EXISTING_PROJECT_URL);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	/**
	 * Helper method for fake posting of form data to update project route
	 * @param id Id of project to update
	 * @param name Name of project (fake form field value)
	 * @param url Project clone URL (fake form field value)
	 * @return Result Response from server
	 */
	private Result postUpdateProject(long id, String name, String url) {

		Map<String,String> formData = new HashMap<String,String>();
		formData.put("name", name);
		formData.put("url", url);

		FakeRequest request = new FakeRequest(POST, "/projects/update/" + id);
		Result result = callAction(controllers.routes.ref.Projects.update(id), request.withFormUrlEncodedBody(formData));

		return result;
	}

}
