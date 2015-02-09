package projects.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import models.OutboundProject;

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
		Result result = callAction(controllers.routes.ref.OutboundProjects.add(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testCreateProject() {
		Result result = postCreateProject(TEST_NEW_PROJECT_DISPLAY_NAME,
											TEST_EXISTING_PROJECT_PROJECT_KEY,
											TEST_EXISTING_PROJECT_SLUG_REPO);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		OutboundProject project = OutboundProject.FIND.where()
											.eq("displayName", TEST_NEW_PROJECT_DISPLAY_NAME)
											.eq("projectKey", TEST_EXISTING_PROJECT_PROJECT_KEY)
											.eq("repositorySlug", TEST_EXISTING_PROJECT_SLUG_REPO)
											.findUnique();
		assertThat(project).isNotNull();
	}

	@Test
	public void testCreateProjectEmptyName() {
		Result result = postCreateProject("",
										TEST_EXISTING_PROJECT_PROJECT_KEY,
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
											TEST_EXISTING_PROJECT_PROJECT_KEY,
											"");

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testCreateProjectInvalidSpace() {

		Result resultKey = postCreateProject(TEST_NEW_PROJECT_DISPLAY_NAME,
											"includes space",
											TEST_EXISTING_PROJECT_SLUG_REPO);

		Result resultSlug = postCreateProject(TEST_NEW_PROJECT_DISPLAY_NAME,
				TEST_EXISTING_PROJECT_PROJECT_KEY,
				"includes space");

		assertThat(status(resultKey)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(resultKey)).isEqualTo("text/html");

		assertThat(status(resultSlug)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(resultSlug)).isEqualTo("text/html");

	}

	@Test
	public void testCreateProjectInvalidProjectKeyChar() {

		Result resultKey = postCreateProject(TEST_NEW_PROJECT_DISPLAY_NAME,
											"includes?invalidchar",
											TEST_EXISTING_PROJECT_SLUG_REPO);

		Result resultSlug = postCreateProject(TEST_NEW_PROJECT_DISPLAY_NAME,
				TEST_EXISTING_PROJECT_PROJECT_KEY,
				"includes?invalidchar");

		assertThat(status(resultKey)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(resultKey)).isEqualTo("text/html");

		assertThat(status(resultSlug)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(resultSlug)).isEqualTo("text/html");

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
		Result result = callAction(controllers.routes.ref.OutboundProjects.create(), request.withFormUrlEncodedBody(formData));

		return result;
	}

}
