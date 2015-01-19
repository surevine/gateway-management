package projects.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import models.OutboundProject;

import org.junit.BeforeClass;
import org.junit.Ignore;
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
		OutboundProject project = new OutboundProject(TEST_EXISTING_PROJECT_ID,
										TEST_EXISTING_PROJECT_DISPLAY_NAME,
										TEST_EXISTING_PROJECT_PROJECT_KEY,
										TEST_EXISTING_PROJECT_SLUG_REPO);

		project.save();
	}

	@Test
	public void testViewProject() {
		FakeRequest request = new FakeRequest(GET, "/projects/view/" + TEST_EXISTING_PROJECT_ID);
		Result result = callAction(controllers.routes.ref.OutboundProjects.view(TEST_EXISTING_PROJECT_ID), request);

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
		Result result = callAction(controllers.routes.ref.OutboundProjects.view(TEST_NON_EXISTING_PROJECT_ID), request);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testViewProjectAPI() {

		FakeRequest request = new FakeRequest(GET, "/api/project/" + TEST_EXISTING_PROJECT_ID);
		Result result = callAction(controllers.routes.ref.OutboundProjectsAPI.view(TEST_EXISTING_PROJECT_ID), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("application/json");

		// Ensure correct content
		String content = contentAsString(result);
		assertThat(content).contains(TEST_EXISTING_PROJECT_DISPLAY_NAME);
		assertThat(content).contains(TEST_EXISTING_PROJECT_PROJECT_KEY);
		assertThat(content).contains(TEST_EXISTING_PROJECT_SLUG_REPO);

	}

	@Test
	public void testNonExistingProjectAPI() {

		FakeRequest request = new FakeRequest(GET, "/api/project/" + TEST_NON_EXISTING_PROJECT_ID);
		Result result = callAction(controllers.routes.ref.OutboundProjectsAPI.view(TEST_NON_EXISTING_PROJECT_ID), request);

		assertThat(status(result)).isEqualTo(NOT_FOUND);

	}

}
