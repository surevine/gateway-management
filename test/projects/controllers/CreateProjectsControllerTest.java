package projects.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import models.Project;

import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;
import projects.ProjectTest;

/**
 * Tests for the project controller create functionality (routes/actions)
 *
 * @author jonnyheavey
 *
 */
public class CreateProjectsControllerTest extends ProjectTest {

	@Test
	public void testAddProject() {
		FakeRequest request = new FakeRequest(GET, "/projects/add");
		Result result = callAction(controllers.routes.ref.Projects.add(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testCreateProject() {
		Result result = postCreateProject(TEST_NEW_PROJECT_DISPLAY_NAME,
											TEST_EXISTING_PROJECT_PROJECT_KEY_NAME,
											TEST_EXISTING_PROJECT_SLUG_REPO);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		Project project = Project.find.where()
											.eq("displayName", TEST_NEW_PROJECT_DISPLAY_NAME)
											.eq("projectKey", TEST_EXISTING_PROJECT_PROJECT_KEY_NAME)
											.eq("repositorySlug", TEST_EXISTING_PROJECT_SLUG_REPO)
											.findUnique();
		assertThat(project).isNotNull();
	}

	@Test
	public void testCreateProjectEmptyName() {
		Result result = postCreateProject("",
										TEST_EXISTING_PROJECT_PROJECT_KEY_NAME,
										TEST_EXISTING_PROJECT_SLUG_REPO);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testCreateProjectEmptyProjectKey() {
		Result result = postCreateProject(TEST_NEW_PROJECT_DISPLAY_NAME,
											"",
											TEST_EXISTING_PROJECT_SLUG_REPO);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testCreateProjectEmptyRepoSlug() {
		Result result = postCreateProject(TEST_NEW_PROJECT_DISPLAY_NAME,
											TEST_EXISTING_PROJECT_PROJECT_KEY_NAME,
											"");

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}


	/**
	 * Helper method for fake posting of form data to create project route
	 * @param displayName Name of project (fake form field value)
	 * @param projectKey URL slug for project name (fake form field value)
	 * @param repositorySlug URL slug for repo name (fake form field value)
	 * @return Result Response from server
	 */
	private Result postCreateProject(String displayName, String projectKey, String repositorySlug) {
		Map<String,String> formData = new HashMap<String,String>();
		formData.put("displayName", displayName);
		formData.put("projectKey", projectKey);
		formData.put("repositorySlug", repositorySlug);

		FakeRequest request = new FakeRequest(POST, "/projects/add");
		Result result = callAction(controllers.routes.ref.Projects.create(), request.withFormUrlEncodedBody(formData));

		return result;
	}

}
