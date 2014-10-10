package destinations.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentType;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;

import destinations.DestinationTest;

public class ListDestinationsControllerTest extends DestinationTest {

	@Test
	public void testListDestinations() {
		FakeRequest request = new FakeRequest(GET, "/destinations");
		Result result = callAction(controllers.routes.ref.Destinations.list(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testListDestinationsAPI() {
		FakeRequest request = new FakeRequest(GET, "/api/destinations");
		Result result = callAction(controllers.routes.ref.DestinationsAPI.list(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("application/json");
	}

}
