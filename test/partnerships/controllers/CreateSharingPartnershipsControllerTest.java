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
import static play.test.Helpers.status;

import java.util.HashMap;
import java.util.Map;

import models.Destination;
import models.Project;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;
import projects.ProjectTest;

import destinations.DestinationTest;

public class CreateSharingPartnershipsControllerTest extends DestinationTest {

	/**
	 * Create existing destination and project in database (for use by tests)
	 */
	@BeforeClass
	public static void createExistingTestData() {
		Destination destination = new Destination(TEST_EXISTING_DESTINATION_ID,
													TEST_EXISTING_DESTINATION_NAME,
													TEST_EXISTING_DESTINATION_URL);
		destination.save();

		Project project = new Project(ProjectTest.TEST_EXISTING_PROJECT_ID,
										ProjectTest.TEST_EXISTING_PROJECT_DISPLAY_NAME,
										ProjectTest.TEST_EXISTING_PROJECT_PROJECT_KEY,
										ProjectTest.TEST_EXISTING_PROJECT_SLUG_REPO);
		project.save();
	}

	@Test
	public void testAddProjectToDestinationPage() {
		Destination destination = Destination.find.byId(TEST_EXISTING_DESTINATION_ID);

		FakeRequest request = new FakeRequest(GET, "/destinations/" + destination.id + "/addproject");
		Result result = callAction(controllers.routes.ref.Destinations.shareProjectPage(destination.id), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains(TEST_EXISTING_DESTINATION_NAME);
	}

	@Test
	public void testCreateSharingPartnership() {

		Destination destination = Destination.find.byId(TEST_EXISTING_DESTINATION_ID);
		Project project = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);

		Result result = postCreateSharingPartnership(destination.id, project.id);

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		assertThat(destination.projects.contains(project)).isEqualTo(true);
	}

	@Test
	public void testCreateSharingPartnershipNonExistingDestination() {
		Result result = postCreateSharingPartnership(TEST_NON_EXISTING_DESTINATION_ID, ProjectTest.TEST_EXISTING_PROJECT_ID);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testCreateSharingPartnershipNonExistingProject() {
		Result result = postCreateSharingPartnership(TEST_EXISTING_DESTINATION_ID, ProjectTest.TEST_NON_EXISTING_PROJECT_ID);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	/**
	 * Helper method for fake posting of form data to create sharing partnership route
	 * @param destinationId id of destination to add project to
	 * @param projectId id of project to add to destinaton
	 * @return
	 */
	private Result postCreateSharingPartnership(Long destinationId, Long projectId) {

		Map<String,String> formData = new HashMap<String,String>();
		formData.put("destinationId", destinationId.toString());
		formData.put("projectId", projectId.toString());

		FakeRequest request = new FakeRequest(POST, "/partnerships/create");
		Result result = callAction(controllers.routes.ref.SharingPartnerships.create(), request.withFormUrlEncodedBody(formData));

		return result;
	}

}
