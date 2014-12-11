package destinations.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.test.Helpers.GET;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.status;
import models.Destination;

import org.junit.BeforeClass;
import org.junit.Test;

import destinations.DestinationTest;

import play.mvc.Result;
import play.test.FakeRequest;

public class ViewDestinationsControllerTest extends DestinationTest {

	/**
	 * Create existing destination in database (for use by tests)
	 */
	@BeforeClass
	public static void createExistingTestDestination() {

		Destination destination = new Destination(
				TEST_EXISTING_DESTINATION_ID,
				TEST_EXISTING_DESTINATION_NAME,
				TEST_EXISTING_DESTINATION_URL);

		destination.save();

	}

	@Test
	public void testViewDestination() {

		FakeRequest request = new FakeRequest(GET, "/destinations/view/" + TEST_EXISTING_DESTINATION_ID);
		Result result = callAction(controllers.routes.ref.Destinations.view(TEST_EXISTING_DESTINATION_ID), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains(TEST_EXISTING_DESTINATION_NAME);
		assertThat(content).contains(TEST_EXISTING_DESTINATION_URL);

	}

	@Test
	public void testNonExistingDestination() {

		FakeRequest request = new FakeRequest(GET, "/destinations/view/" + TEST_NON_EXISTING_DESTINATION_ID);
		Result result = callAction(controllers.routes.ref.Destinations.view(TEST_NON_EXISTING_DESTINATION_ID), request);

		assertThat(status(result)).isEqualTo(NOT_FOUND);

	}

}
