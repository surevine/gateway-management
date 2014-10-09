package destinations.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import models.Destination;

import org.junit.BeforeClass;
import org.junit.Test;

import destinations.DestinationTest;

import play.mvc.Result;
import play.test.FakeRequest;

/**
 * Tests for the destination controller delete functionality (routes/actions)
 *
 * @author jonnyheavey
 *
 */
public class DeleteDestinationsControllerTest extends DestinationTest {

	/**
	 * Create existing destination in database (for use by delete tests)
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
	public void testDeleteDestination() {
		Result result = postDeleteDestination(TEST_EXISTING_DESTINATION_ID);

		// Expect 303 as implementation redirects to 'list' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);
	}

	@Test
	public void testDeleteNonExistingDestination() {
		Result result = postDeleteDestination(TEST_NON_EXISTING_DESTINATION_ID);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	/**
	 * Helper method for fake posting to delete destination route
	 * @param id Id of destination to delete
	 * @return Result Response from server
	 */
	private Result postDeleteDestination(long id) {

		FakeRequest request = new FakeRequest(POST, "/destinations/delete/" + id);
		Result result = callAction(controllers.routes.ref.Destinations.delete(id), request);

		return result;
	}

}
