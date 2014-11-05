package partnerships.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.POST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;

import java.util.HashMap;
import java.util.Map;

import models.Destination;
import models.Project;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import play.Logger;
import play.mvc.Result;
import play.test.FakeRequest;
import projects.ProjectTest;

import destinations.DestinationTest;

public class DeleteSharingPartnershipsControllerTest extends DestinationTest {

	/**
	 * Create existing destination and project in database (for use by tests)
	 */
	@BeforeClass
	public static void createExistingTestData() {
		Destination destination = new Destination(TEST_EXISTING_DESTINATION_ID,
													TEST_EXISTING_DESTINATION_NAME,
													TEST_EXISTING_DESTINATION_URL);
		destination.save();

		Project project1 = new Project(ProjectTest.TEST_EXISTING_PROJECT_ID,
										ProjectTest.TEST_EXISTING_PROJECT_DISPLAY_NAME,
										ProjectTest.TEST_EXISTING_PROJECT_PROJECT_KEY,
										ProjectTest.TEST_EXISTING_PROJECT_SLUG_REPO);

		Project project2 = new Project(ProjectTest.TEST_NEW_PROJECT_ID,
										ProjectTest.TEST_NEW_PROJECT_DISPLAY_NAME,
										ProjectTest.TEST_NEW_PROJECT_PROJECT_KEY,
										ProjectTest.TEST_NEW_PROJECT_SLUG_REPO);

		project1.save();
		project2.save();

		destination.addProject(project1);
	}

	@Test
	public void testDeleteSharingPartnershipFromDestination() {

		Destination destination = Destination.find.byId(TEST_EXISTING_DESTINATION_ID);
		Project project = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);
		destination.addProject(project);
		destination.update();

		Result result = postDeleteSharingPartnershipFromDestination(destination.id, project.id);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		Destination updatedDestination = Destination.find.byId(TEST_EXISTING_DESTINATION_ID);
		assertThat(updatedDestination.projects.contains(project)).isEqualTo(false);

	}

	@Test
	public void testDeleteSharingPartnershipFromProject() {

		Destination destination = Destination.find.byId(TEST_EXISTING_DESTINATION_ID);
		Project project = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);
		destination.addProject(project);
		destination.update();

		Result result = postDeleteSharingPartnershipFromProject(project.id, destination.id);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		Project updatedProject = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);
		assertThat(updatedProject.destinations.contains(destination)).isEqualTo(false);
	}

	@Test
	public void testDeleteNonExistingSharingPartnershipFromDestination() {

		Destination destination = Destination.find.byId(TEST_EXISTING_DESTINATION_ID);
		Project project = Project.find.byId(ProjectTest.TEST_NEW_PROJECT_ID);

		Result result = postDeleteSharingPartnershipFromDestination(destination.id, project.id);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testDeleteNonExistingSharingPartnershipFromProject() {

		Destination destination = Destination.find.byId(TEST_EXISTING_DESTINATION_ID);
		Project project = Project.find.byId(ProjectTest.TEST_NEW_PROJECT_ID);

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
