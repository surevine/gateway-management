package partnerships.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.status;
import static play.test.Helpers.stop;

import java.util.HashMap;
import java.util.Map;

import models.Destination;
import models.Project;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import play.Logger;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import projects.ProjectTest;

import destinations.DestinationTest;

public class CreateSharingPartnershipsControllerTest {

	public static FakeApplication app;

	@BeforeClass
	public static void setup() {

		app = fakeApplication(inMemoryDatabase());
		start(app);

		DestinationTest.createTestDestinationDirectory();

		Destination destination = new Destination(DestinationTest.TEST_EXISTING_DESTINATION_ID,
													DestinationTest.TEST_EXISTING_DESTINATION_NAME,
													DestinationTest.TEST_EXISTING_DESTINATION_URL);
		destination.save();

		Project project = new Project(ProjectTest.TEST_EXISTING_PROJECT_ID,
										ProjectTest.TEST_EXISTING_PROJECT_DISPLAY_NAME,
										ProjectTest.TEST_EXISTING_PROJECT_PROJECT_KEY,
										ProjectTest.TEST_EXISTING_PROJECT_SLUG_REPO);
		project.save();
	}

	@AfterClass
	public static void teardown() {
		stop(app);
		DestinationTest.destroyTestDestinationDirectory();
	}

	@Test
	public void testAddProjectsToDestinationPage() {
		Destination destination = Destination.find.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);

		FakeRequest request = new FakeRequest(GET, "/destinations/" + destination.id + "/shareprojects");
		Result result = callAction(controllers.routes.ref.Destinations.shareProjectPage(destination.id), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains(DestinationTest.TEST_EXISTING_DESTINATION_NAME);
	}

	@Test
	public void testAddDestinationsToProjectPage() {
		Project project = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);

		FakeRequest request = new FakeRequest(GET, "/projects/" + project.id + "/shareproject");
		Result result = callAction(controllers.routes.ref.Projects.shareProjectPage(project.id), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains(ProjectTest.TEST_EXISTING_PROJECT_DISPLAY_NAME);
	}

	@Test
	public void testCreateSharingPartnershipFromDestination() {

		Destination destination = Destination.find.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);
		Project project = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);

		Result result = postCreateSharingPartnershipFromDestination(destination.id, project.id);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		assertThat(destination.projects.contains(project)).isEqualTo(true);
	}

	@Test
	public void testCreateSharingPartnershipFromProject() {

		Destination destination = Destination.find.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);
		Project project = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);

		// Tidy data from previous tests if required
		if(project.destinations.contains(destination)) {
			project.destinations.remove(destination);
			project.update();
		}

		Result result = postCreateSharingPartnershipFromProject(project.id, destination.id);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		Project updatedProject = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);
		assertThat(updatedProject.destinations.contains(destination)).isEqualTo(true);
	}

	@Test
	public void testCreateSharingPartnershipNonExistingDestination() {
		Result result = postCreateSharingPartnershipFromDestination(DestinationTest.TEST_NON_EXISTING_DESTINATION_ID, ProjectTest.TEST_EXISTING_PROJECT_ID);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testCreateSharingPartnershipNonExistingProject() {
		Result result = postCreateSharingPartnershipFromDestination(DestinationTest.TEST_EXISTING_DESTINATION_ID, ProjectTest.TEST_NON_EXISTING_PROJECT_ID);

		// Expecting silent fail
		assertThat(status(result)).isEqualTo(SEE_OTHER);
	}

	/**
	 * Helper method for fake posting of form data to create sharing partnership route.
	 * Simulates post from destination view page.
	 *
	 * @param destinationId id of destination to add project to
	 * @param projectId id of project to add to destinaton
	 * @return
	 */
	private Result postCreateSharingPartnershipFromDestination(Long destinationId, Long projectId) {

		Map<String,String> formData = new HashMap<String,String>();
		formData.put("source", "destination");
		formData.put("destinationId", destinationId.toString());
		formData.put("selectedProjects", projectId.toString());

		FakeRequest request = new FakeRequest(POST, "/partnerships/create");
		Result result = callAction(controllers.routes.ref.SharingPartnerships.create(), request.withFormUrlEncodedBody(formData));

		return result;
	}

	/**
	 * Helper method for fake posting of form data to create sharing partnership route.
	 * Simulates post from project view page.
	 *
	 * @param projectId id of project to add to destinaton
	 * @param destinationId id of destination to add project to
	 * @return
	 */
	private Result postCreateSharingPartnershipFromProject(Long projectId, Long destinationId) {

		Map<String,String> formData = new HashMap<String,String>();
		formData.put("source", "project");
		formData.put("projectId", projectId.toString());
		formData.put("selectedDestinations", destinationId.toString());

		FakeRequest request = new FakeRequest(POST, "/partnerships/create");
		Result result = callAction(controllers.routes.ref.SharingPartnerships.create(), request.withFormUrlEncodedBody(formData));

		return result;
	}

}
