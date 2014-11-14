package com.surevine.gateway.auth;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.util.Collections;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.api.mvc.RequestHeader;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.test.FakeApplication;

import controllers.RemoteAuthenticator;

public class WildflyRemoteAuthenticatorTest {

	public static FakeApplication app;

	public static final String EXISTING_SESSION_USERNAME = "existing-username";
	public static final String WILDFLY_AUTHENTICATED_USERNAME = "wildfly-username";

	private RemoteAuthenticator fixture = new RemoteAuthenticator();

	@BeforeClass
	public static void setup() throws Exception {
		app = fakeApplication(inMemoryDatabase());
		start(app);

		// Set HTTP context
		Map<String, String> flashData = Collections.emptyMap();
	    Map<String, Object> argData = Collections.emptyMap();
	    Long id = 2L;
	    Request request = mock(Request.class);
	    RequestHeader header = mock(RequestHeader.class);
	    Context context = new Context(id, header, request, flashData, flashData, argData);
	    Context.current.set(context);
	}

	@AfterClass
	public static void teardown() {
		stop(app);
	}

	@Test
	public void testGetUsernameExistingSession() {

		Context ctx = Context.current();
		ctx.session().put("username", EXISTING_SESSION_USERNAME);

		String authenticatedUser = fixture.getUsername(ctx);

		assertThat(authenticatedUser).isEqualTo(EXISTING_SESSION_USERNAME);

		// Tidy session after test
		ctx.session().remove("username");
	}

	@Test
	public void testGetUsernameNewSession() {

		Context ctx = Context.current();

		AuthServiceProxy mockAuthServiceProxy = mock(WildflyAuthServiceProxy.class);
		try {
			when(mockAuthServiceProxy.getAuthenticatedUsername()).thenReturn(WILDFLY_AUTHENTICATED_USERNAME);
		} catch (AuthServiceProxyException e) {
			// Not expecting to catch this due to mock.
			fail();
		}

		fixture.setAuthServiceProxy(mockAuthServiceProxy);

		String authenticatedUser = fixture.getUsername(ctx);

		assertThat(authenticatedUser).isEqualTo(WILDFLY_AUTHENTICATED_USERNAME);
		assertThat(ctx.session().get("username")).isEqualTo(WILDFLY_AUTHENTICATED_USERNAME);

		// Tidy session after test
		ctx.session().remove("username");
	}

	@Test
	public void testGetUsernameUnauthenticatedUser() {

		AuthServiceProxy mockUnauthenticatedAuthServiceProxy = mock(WildflyAuthServiceProxy.class);
		try {
			// AuthServiceProxy returns null if no authenticated user found
			when(mockUnauthenticatedAuthServiceProxy.getAuthenticatedUsername()).thenReturn(null);
		} catch (AuthServiceProxyException e) {
			// Not expecting to catch this due to mock.
			fail();
		}

		fixture.setAuthServiceProxy(mockUnauthenticatedAuthServiceProxy);

		String authenticatedUser = fixture.getUsername(Context.current());

		assertThat(authenticatedUser).isNull();
	}

	@Test
	public void testGetUsernameServiceDown() {

		// Service already down so no need to mock

		String authenticatedUser = fixture.getUsername(Context.current());

		assertThat(authenticatedUser).isNull();
	}

}
