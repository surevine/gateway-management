package projects.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import models.Project;

import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;
import projects.ProjectTest;

public class ViewProjectsControllerTest extends ProjectTest {

	/**
	 * Create existing project in database (for use by tests)
	 */
	@BeforeClass
	public static void createExistingTestProject() {
		Project project = new Project(TEST_EXISTING_PROJECT_ID,
										TEST_EXISTING_PROJECT_DISPLAY_NAME,
										TEST_EXISTING_PROJECT_PROJECT_KEY,
										TEST_EXISTING_PROJECT_SLUG_REPO);

		project.save();
	}

	@Test
	public void testViewProject() {
		FakeRequest request = new FakeRequest(GET, "/projects/view/" + TEST_EXISTING_PROJECT_ID);
		Result result = callAction(controllers.routes.ref.Projects.view(TEST_EXISTING_PROJECT_ID), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains(TEST_EXISTING_PROJECT_DISPLAY_NAME);
		assertThat(content).contains(TEST_EXISTING_PROJECT_PROJECT_KEY);
		assertThat(content).contains(TEST_EXISTING_PROJECT_SLUG_REPO);
	}

	@Test
	public void testNonExistingProjectView() {
		FakeRequest request = new FakeRequest(GET, "/projects/view/" + TEST_NON_EXISTING_PROJECT_ID);
		Result result = callAction(controllers.routes.ref.Projects.view(TEST_NON_EXISTING_PROJECT_ID), request);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

}
