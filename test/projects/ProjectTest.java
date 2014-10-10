package projects;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import play.test.FakeApplication;

/**
 * Common test setup / config for Project tests.
 * Used by other project test classes.
 *
 * @author jonnyheavey
 *
 */
public class ProjectTest {

	public static FakeApplication app;

	public static long TEST_NEW_PROJECT_ID = 2014;
	public static String TEST_NEW_PROJECT_NAME = "Project B";
	public static String TEST_NEW_PROJECT_URL = "git@github.com:surevine/project-b.git";

	public static long TEST_EXISTING_PROJECT_ID = 2013;
	public static String TEST_EXISTING_PROJECT_NAME = "Project A";
	public static String TEST_EXISTING_PROJECT_URL = "git@github.com:surevine/project-a.git";

	public static String TEST_INVALID_URL = "/invalid";
	public static long TEST_NON_EXISTING_PROJECT_ID = 10000;

	@BeforeClass
	public static void setup() {
		app = fakeApplication(inMemoryDatabase());
		start(app);
	}

	@AfterClass
	public static void teardown() {
		stop(app);
	}

}
