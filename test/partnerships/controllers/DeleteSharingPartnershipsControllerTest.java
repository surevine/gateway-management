package partnerships.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.POST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.status;
import static play.test.Helpers.stop;

import java.util.HashMap;
import java.util.Map;

import models.Destination;
import models.OutboundProject;

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

public class DeleteSharingPartnershipsControllerTest {

	public static FakeApplication app;

	@BeforeClass
	public static void createExistingTestData() {

		app = fakeApplication(inMemoryDatabase());
		start(app);

		DestinationTest.createTestDestinationDirectory();

		Destination destination = new Destination(DestinationTest.TEST_EXISTING_DESTINATION_ID,
													DestinationTest.TEST_EXISTING_DESTINATION_NAME,
													DestinationTest.TEST_EXISTING_DESTINATION_URL);
		destination.save();

		OutboundProject project1 = new OutboundProject(ProjectTest.TEST_EXISTING_PROJECT_ID,
										ProjectTest.TEST_EXISTING_PROJECT_DISPLAY_NAME,
										ProjectTest.TEST_EXISTING_PROJECT_PROJECT_KEY,
										ProjectTest.TEST_EXISTING_PROJECT_SLUG_REPO);

		OutboundProject project2 = new OutboundProject(ProjectTest.TEST_NEW_PROJECT_ID,
										ProjectTest.TEST_NEW_PROJECT_DISPLAY_NAME,
										ProjectTest.TEST_NEW_PROJECT_PROJECT_KEY,
										ProjectTest.TEST_NEW_PROJECT_SLUG_REPO);

		project1.save();
		project2.save();

		destination.addProject(project1);
	}

	@AfterClass
	public static void teardown() {
		stop(app);
		DestinationTest.destroyTestDestinationDirectory();
	}

	@Test
	public void testDeleteSharingPartnershipFromDestination() {

		Destination destination = Destination.FIND.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);
		OutboundProject project = OutboundProject.FIND.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);
		destination.addProject(project);
		destination.update();

		Result result = postDeleteSharingPartnershipFromDestination(destination.id, project.id);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		Destination updatedDestination = Destination.FIND.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);
		assertThat(updatedDestination.projects.contains(project)).isEqualTo(false);

	}

	@Test
	public void testDeleteSharingPartnershipFromProject() {

		Destination destination = Destination.FIND.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);
		OutboundProject project = OutboundProject.FIND.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);
		destination.addProject(project);
		destination.update();

		Result result = postDeleteSharingPartnershipFromProject(project.id, destination.id);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		OutboundProject updatedProject = OutboundProject.FIND.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);
		assertThat(updatedProject.destinations.contains(destination)).isEqualTo(false);
	}

	@Test
	public void testDeleteNonExistingSharingPartnershipFromDestination() {

		Destination destination = Destination.FIND.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);
		OutboundProject project = OutboundProject.FIND.byId(ProjectTest.TEST_NEW_PROJECT_ID);

		Result result = postDeleteSharingPartnershipFromDestination(destination.id, project.id);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testDeleteNonExistingSharingPartnershipFromProject() {

		Destination destination = Destination.FIND.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);
		OutboundProject project = OutboundProject.FIND.byId(ProjectTest.TEST_NEW_PROJECT_ID);

		Result result = postDeleteSharingPartnershipFromProject(project.id, destination.id);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	/**
	 * Helper method for fake posting of form data to delete sharing partnership route (from destination)
	 * @param destinationId id of destination to remove project sharing from
	 * @param projectId id of project to stop sharing
	 * @return
	 */
	private Result postDeleteSharingPartnershipFromDestination(Long destinationId, Long projectId) {

		Map<String,String> formData = new HashMap<String,String>();
		formData.put("source", "destination");
		formData.put("destinationId", destinationId.toString());
		formData.put("projectId", projectId.toString());

		FakeRequest request = new FakeRequest(POST, "/partnerships/delete");
		Result result = callAction(controllers.routes.ref.SharingPartnerships.delete(), request.withFormUrlEncodedBody(formData));

		return result;
	}

	/**
	 * Helper method for fake posting of form data to delete sharing partnership route (from project)
	 * @param destinationId id of destination to remove project sharing from
	 * @param projectId id of project to stop sharing
	 * @return
	 */
	private Result postDeleteSharingPartnershipFromProject(Long projectId, Long destinationId) {

		Map<String,String> formData = new HashMap<String,String>();
		formData.put("source", "project");
		formData.put("projectId", projectId.toString());
		formData.put("destinationId", destinationId.toString());

		FakeRequest request = new FakeRequest(POST, "/partnerships/delete");
		Result result = callAction(controllers.routes.ref.SharingPartnerships.delete(), request.withFormUrlEncodedBody(formData));

		return result;
	}

}
