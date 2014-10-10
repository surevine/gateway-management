package destinations.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import models.Destination;

import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;

import destinations.DestinationTest;

public class ListDestinationsControllerTest extends DestinationTest {

	/**
	 * Create existing destination in database (for use by tests)
	 */
	@BeforeClass
	public static void createExistingTestDestination() {
		Destination destination = new Destination();
		destination.id = TEST_EXISTING_DESTINATION_ID;
		destination.name = TEST_EXISTING_DESTINATION_NAME;
		destination.url = TEST_EXISTING_DESTINATION_URL;
		destination.save();
	}

	@Test
	public void testListDestinations() {
		FakeRequest request = new FakeRequest(GET, "/destinations");
		Result result = callAction(controllers.routes.ref.Destinations.list(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains(TEST_EXISTING_DESTINATION_NAME);
		assertThat(content).contains(TEST_EXISTING_DESTINATION_URL);
	}

	@Test
	public void testListDestinationsAPI() {
		FakeRequest request = new FakeRequest(GET, "/api/destinations");
		Result result = callAction(controllers.routes.ref.DestinationsAPI.list(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("application/json");

		String content = contentAsString(result);
		assertThat(content).contains(TEST_EXISTING_DESTINATION_NAME);
		assertThat(content).contains(TEST_EXISTING_DESTINATION_URL);
	}

}