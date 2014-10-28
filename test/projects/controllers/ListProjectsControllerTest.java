package projects.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import models.Destination;
import models.Project;

import org.junit.BeforeClass;
import org.junit.Test;

import destinations.DestinationTest;

import play.mvc.Result;
import play.test.FakeRequest;
import projects.ProjectTest;
import scala.collection.mutable.Set;

public class ListProjectsControllerTest extends ProjectTest {

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

		List<Project> destinationProjects = new ArrayList<Project>();
		destinationProjects.add(project);

		Destination destination = new Destination(DestinationTest.TEST_EXISTING_DESTINATION_ID,
												DestinationTest.TEST_EXISTING_DESTINATION_NAME,
												DestinationTest.TEST_EXISTING_DESTINATION_URL,
												destinationProjects);
		destination.save();

		Project unsharedProject = new Project(TEST_NEW_PROJECT_ID,
											TEST_NEW_PROJECT_DISPLAY_NAME,
											TEST_NEW_PROJECT_PROJECT_KEY,
											TEST_NEW_PROJECT_SLUG_REPO);
		unsharedProject.save();
	}

	@Test
	public void testListProjects() {
		FakeRequest request = new FakeRequest(GET, "/projects");
		Result result = callAction(controllers.routes.ref.Projects.list(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains(TEST_EXISTING_PROJECT_DISPLAY_NAME);
		assertThat(content).contains(TEST_EXISTING_PROJECT_PROJECT_KEY);
		assertThat(content).contains(TEST_EXISTING_PROJECT_SLUG_REPO);
	}

	@Test
	public void testListProjectsAPI() {
		FakeRequest request = new FakeRequest(GET, "/api/projects");
		Result result = callAction(controllers.routes.ref.ProjectsAPI.list(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("application/json");

		// Ensure shared project is included in response
		String content = contentAsString(result);
		assertThat(content).contains(TEST_EXISTING_PROJECT_DISPLAY_NAME);
		assertThat(content).contains(TEST_EXISTING_PROJECT_PROJECT_KEY);
		assertThat(content).contains(TEST_EXISTING_PROJECT_SLUG_REPO);

		// Ensure unshared project not included in response
		assertThat(content).doesNotContain(TEST_NEW_PROJECT_DISPLAY_NAME);
		assertThat(content).doesNotContain(TEST_NEW_PROJECT_SLUG_REPO);

	}

}
