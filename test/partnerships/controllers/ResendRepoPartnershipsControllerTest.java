package partnerships.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.INTERNAL_SERVER_ERROR;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
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
import models.Project;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import play.api.test.Helpers;
import play.mvc.HandlerRef;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import projects.ProjectTest;

import com.surevine.gateway.scm.service.SCMFederatorServiceException;
import com.surevine.gateway.scm.service.SCMFederatorServiceFacade;

import controllers.AuditedController;
import controllers.SharingPartnerships;
import destinations.DestinationTest;

public class ResendRepoPartnershipsControllerTest {

	public static FakeApplication app;

	private static final Long NON_EXISTING_PROJECT_ID = 1000L;
	private static final Long NON_EXISTING_DESTINATION_ID = 1001L;

	@BeforeClass
	public static void createExistingTestData() {

		app = fakeApplication(inMemoryDatabase());
		start(app);

		DestinationTest.createTestDestinationDirectory();

		Destination destination = new Destination(DestinationTest.TEST_EXISTING_DESTINATION_ID,
													DestinationTest.TEST_EXISTING_DESTINATION_NAME,
													DestinationTest.TEST_EXISTING_DESTINATION_URL);
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

	@AfterClass
	public static void teardown() {
		stop(app);
		DestinationTest.destroyTestDestinationDirectory();
	}

	@Test
	public void testResendRepo() {

		SCMFederatorServiceFacade mockSCMService = mock(SCMFederatorServiceFacade.class);
		try {
			doNothing().when(mockSCMService).resend(anyString(), anyString(), anyString());
		} catch (SCMFederatorServiceException e) {
			// not expecting to be thrown due to mocking
			fail();
		}

		SharingPartnerships.setSCMFederator(mockSCMService);

		Destination destination = Destination.find.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);
		Project project = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);

		Result result = postResend(destination.id, project.id);

		assertThat(status(result)).isEqualTo(OK);

	}

	@Test
	public void testResendRepoFederatorDown() {

		Destination destination = Destination.find.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);
		Project project = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);

		Result result = postResend(destination.id, project.id);

		assertThat(status(result)).isEqualTo(INTERNAL_SERVER_ERROR);

	}

	@Test
	public void testResendRepoNonExistingProject() {
		Destination destination = Destination.find.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);

		Result result = postResend(destination.id, NON_EXISTING_PROJECT_ID);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testResendRepoNonExistingDestination() {
		Project project = Project.find.byId(ProjectTest.TEST_EXISTING_PROJECT_ID);

		Result result = postResend(NON_EXISTING_DESTINATION_ID, project.id);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testResendUnsharedProject() {
		Destination destination = Destination.find.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);
		Project project = Project.find.byId(ProjectTest.TEST_NEW_PROJECT_ID);

		Result result = postResend(destination.id, project.id);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	/**
	 * Helper method to post to resend action
	 */
	private Result postResend(Long destinationId, Long projectId) {

		Map<String,String> formData = new HashMap<String,String>();
		formData.put("destinationId", destinationId.toString());
		formData.put("projectId", projectId.toString());

		FakeRequest request = new FakeRequest(POST, "/partnerships/resend");
		Result result = callAction(controllers.routes.ref.SharingPartnerships.resend(), request.withFormUrlEncodedBody(formData));

		return result;

	}

}
