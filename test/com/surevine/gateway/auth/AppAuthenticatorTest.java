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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.surevine.gateway.auditing.LogfileAuditServiceImpl;
import com.surevine.gateway.auditing.action.AuditActionFactory;
import com.surevine.gateway.auditing.action.LogfileAuditActionFactory;

import play.Logger;
import play.api.mvc.RequestHeader;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.test.FakeApplication;

import controllers.AppAuthenticator;

public class AppAuthenticatorTest {

	public static FakeApplication app;

	public static final String EXISTING_SESSION_USERNAME = "existing-username";
	public static final String WILDFLY_AUTHENTICATED_USERNAME = "wildfly-username";

	private AppAuthenticator fixture = new AppAuthenticator();

	@BeforeClass
	public static void setup() throws Exception {
		app = fakeApplication(inMemoryDatabase());
		start(app);
	}

	@AfterClass
	public static void teardown() {
		stop(app);
	}

	@Before
	public void beforeTest() {
		setupContext();
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

		AuthService mockAuthService = mock(WildflyAuthService.class);
		try {
			when(mockAuthService.getAuthenticatedUsername()).thenReturn(WILDFLY_AUTHENTICATED_USERNAME);
		} catch (AuthServiceException e) {
			// Not expecting to catch this due to mock.
			fail();
		}

		fixture.setAuthService(mockAuthService);

		String authenticatedUser = fixture.getUsername(ctx);

		assertThat(authenticatedUser).isEqualTo(WILDFLY_AUTHENTICATED_USERNAME);
		assertThat(ctx.session().get("username")).isEqualTo(WILDFLY_AUTHENTICATED_USERNAME);

		// Tidy session after test
		ctx.session().remove("username");
	}

	@Test
	public void testGetUsernameUnauthenticatedUser() {

		AuthService mockUnauthenticatedAuthService = mock(WildflyAuthService.class);
		try {
			// AuthService returns null if no authenticated user found
			when(mockUnauthenticatedAuthService.getAuthenticatedUsername()).thenReturn(null);
		} catch (AuthServiceException e) {
			// Not expecting to catch this due to mock.
			fail();
		}

		fixture.setAuthService(mockUnauthenticatedAuthService);

		String authenticatedUser = fixture.getUsername(Context.current());

		assertThat(authenticatedUser).isNull();
	}

	@Test
	public void testGetUsernameServiceDown() {

		AuthService mockOfflineAuthService = mock(WildflyAuthService.class);
		fixture.setAuthService(mockOfflineAuthService);

		String authenticatedUser = fixture.getUsername(Context.current());

		assertThat(authenticatedUser).isNull();
	}

	/**
	 * Helper method to reset http context for test execution
	 */
	private void setupContext() {
		Map<String, String> flashData = Collections.emptyMap();
	    Map<String, Object> argData = Collections.emptyMap();
	    Long id = 2L;
	    Request request = mock(Request.class);
	    RequestHeader header = mock(RequestHeader.class);
	    Context context = new Context(id, header, request, flashData, flashData, argData);
	    Context.current.set(context);
	}

}
