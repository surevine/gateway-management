package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;

public class DestinationsTest {

	public static FakeApplication app;

	@BeforeClass
	public static void startApp() {
		app = fakeApplication(inMemoryDatabase());
		start(app);
	}

	@AfterClass
	public static void stopApp() {
		stop(app);
	}

	@Test
	public void testCreateDestination() {

		Result result = postCreateDestination("Surevine", "http://www.surevine.com");

		assertThat(status(result)).isEqualTo(SEE_OTHER);

	}

	@Test
	public void testCreateDestinationEmptyName() {

		Result result = postCreateDestination("", "http://www.surevine.com");

		assertThat(status(result)).isEqualTo(BAD_REQUEST);

	}

	@Test
	public void testCreateDestinationEmptyURL() {

		Result result = postCreateDestination("Surevine", "");

		assertThat(status(result)).isEqualTo(BAD_REQUEST);

	}

	@Test
	public void testCreateDestinationInvalidURL() {

		Result result = postCreateDestination("Surevine", "invalid-url");

		assertThat(status(result)).isEqualTo(BAD_REQUEST);

	}

	private Result postCreateDestination(String name, String url) {

		Map<String,String> formData = new HashMap<String,String>();
		formData.put("name", name);
		formData.put("url", url);

		FakeRequest request = new FakeRequest(POST, "/destinations/add");
		Result result = callAction(controllers.routes.ref.Destinations.create(), request.withFormUrlEncodedBody(formData));

		return result;

	}

}
