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
	public static String TEST_NEW_PROJECT_DISPLAY_NAME = "Project B";
	public static String TEST_NEW_PROJECT_PROJECT_KEY = "partner-b";
	public static String TEST_NEW_PROJECT_SLUG_REPO = "repo-b";

	public static long TEST_EXISTING_PROJECT_ID = 2013;
	public static String TEST_EXISTING_PROJECT_DISPLAY_NAME = "Project A";
	public static String TEST_EXISTING_PROJECT_PROJECT_KEY = "partner-a";
	public static String TEST_EXISTING_PROJECT_SLUG_REPO = "repo-a";

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
