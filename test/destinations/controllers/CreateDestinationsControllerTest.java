package destinations.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import models.Partner;

import org.junit.Test;

import destinations.DestinationTest;

import play.mvc.Result;
import play.test.FakeRequest;

/**
 * Tests for the destination controller create functionality (routes/actions)
 *
 * @author jonnyheavey
 *
 */
public class CreateDestinationsControllerTest extends DestinationTest {

	@Test
	public void testAddDestination() {
		FakeRequest request = new FakeRequest(GET, "/destinations/add");
		Result result = callAction(controllers.routes.ref.Destinations.add(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testCreateDestination() {
		Result result = postCreateDestination(TEST_NEW_DESTINATION_NAME, TEST_NEW_DESTINATION_URL);

		// Expect 303 as implementation redirects to 'list' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		Partner destination = Partner.FIND.where()
													.eq("name", TEST_NEW_DESTINATION_NAME)
													.eq("url", TEST_NEW_DESTINATION_URL)
													.findUnique();
		assertThat(destination).isNotNull();
	}

	@Test
	public void testCreateDestinationEmptyName() {
		Result result = postCreateDestination("", TEST_NEW_DESTINATION_URL);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testCreateDestinationEmptyURL() {
		Result result = postCreateDestination(TEST_NEW_DESTINATION_NAME, "");

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testCreateDestinationInvalidURL() {
		Result result = postCreateDestination(TEST_NEW_DESTINATION_NAME, TEST_INVALID_URL);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	@Test
	public void testCreateDestinationExistingURL() {
		Result result = postCreateDestination(TEST_NEW_DESTINATION_NAME, TEST_NEW_DESTINATION_URL);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		assertThat(contentType(result)).isEqualTo("text/html");
	}

	/**
	 * Helper method for fake posting of form data to create destination route
	 * @param name Name of destination (fake form field value)
	 * @param url URL of destination (fake form field value)
	 * @return Result Response from server
	 */
	private Result postCreateDestination(String name, String url) {
		Map<String,String> formData = new HashMap<String,String>();
		formData.put("name", name);
		formData.put("url", url);

		// TODO incorporate this field in the tests
		formData.put("sourceKey", "blank");

		FakeRequest request = new FakeRequest(POST, "/destinations/add");
		Result result = callAction(controllers.routes.ref.Destinations.create(), request.withFormUrlEncodedBody(formData));

		return result;
	}

}
