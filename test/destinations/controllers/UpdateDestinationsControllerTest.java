package destinations.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import models.Destination;

import org.junit.BeforeClass;
import org.junit.Test;

import destinations.DestinationTest;

import play.mvc.Result;
import play.test.FakeRequest;

/**
 * Tests for the destination controller update functionality (routes/actions)
 *
 * @author jonnyheavey
 *
 */
public class UpdateDestinationsControllerTest extends DestinationTest {

	/**
	 * Create existing destination in database (for use by update tests)
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
	public void testEditDestination() {
		FakeRequest request = new FakeRequest(GET, "/destinations/edit/" + TEST_EXISTING_DESTINATION_ID);
		Result result = callAction(controllers.routes.ref.Destinations.edit(TEST_EXISTING_DESTINATION_ID), request);

		assertThat(status(result)).isEqualTo(OK);
	}

	@Test
	public void testEditNonExistingDestination() {
		FakeRequest request = new FakeRequest(GET, "/destinations/edit/" + TEST_NON_EXISTING_DESTINATION_ID);
		Result result = callAction(controllers.routes.ref.Destinations.edit(TEST_NON_EXISTING_DESTINATION_ID), request);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testUpdateDestination() {

		Result result = postUpdateDestination(TEST_EXISTING_DESTINATION_ID, "Destination A Updated", "file:///tmp/desta/updated");

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

	}

	public void testUpdateDestinationEmptyName() {
		Result result = postUpdateDestination(TEST_EXISTING_DESTINATION_ID, "", TEST_EXISTING_DESTINATION_URL);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}

	public void testUpdateDestinationEmptyURL() {
		Result result = postUpdateDestination(TEST_EXISTING_DESTINATION_ID, TEST_EXISTING_DESTINATION_NAME, "");

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void testUpdateDestinationInvalidURL() {
		Result result = postUpdateDestination(TEST_EXISTING_DESTINATION_ID, TEST_EXISTING_DESTINATION_NAME, TEST_INVALID_URL);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void testUpdateDestinationNonExistingID() {
		Result result = postUpdateDestination(TEST_NON_EXISTING_DESTINATION_ID, TEST_EXISTING_DESTINATION_NAME, TEST_EXISTING_DESTINATION_URL);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	@Test
	public void testDeleteDestination() {

		Result result = postUpdateDestination(TEST_EXISTING_DESTINATION_ID, "Destination A Updated", "file:///tmp/desta/updated");

		// Expect 303 as implementation redirects to 'view' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

	}

	/**
	 * Helper method for fake posting of form data to update destination route
	 * @param id Id of destination to update
	 * @param name Name of destination (fake form field value)
	 * @param url URL of destination (fake form field value)
	 * @return Result Response from server
	 */
	private Result postUpdateDestination(long id, String name, String url) {

		Map<String,String> formData = new HashMap<String,String>();
		formData.put("name", name);
		formData.put("url", url);

		FakeRequest request = new FakeRequest(POST, "/destinations/update/" + id);
		Result result = callAction(controllers.routes.ref.Destinations.update(id), request.withFormUrlEncodedBody(formData));

		return result;
	}

}
