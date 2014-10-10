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

public class ListProjectsControllerTest extends ProjectTest {

	/**
	 * Create existing project in database (for use by tests)
	 */
	@BeforeClass
	public static void createExistingTestProject() {
		Project project = new Project(TEST_EXISTING_PROJECT_ID, TEST_EXISTING_PROJECT_NAME, TEST_EXISTING_PROJECT_URL);
		project.save();
	}

	@Test
	public void testListProjects() {
		FakeRequest request = new FakeRequest(GET, "/projects");
		Result result = callAction(controllers.routes.ref.Projects.list(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains(TEST_EXISTING_PROJECT_NAME);
		assertThat(content).contains(TEST_EXISTING_PROJECT_URL);
	}

}
