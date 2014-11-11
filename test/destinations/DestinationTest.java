package destinations;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import models.Destination;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import play.test.FakeApplication;

/**
 * Common test setup / config for Destination tests.
 * Used by other destination test classes.
 *
 * @author jonnyheavey
 *
 */
public class DestinationTest {

	public static FakeApplication app;

	public static String TEST_DESTINATIONS_DIR = Destination.DESTINATIONS_RULES_DIRECTORY;

	public static long TEST_NEW_DESTINATION_ID = 2014;
	public static String TEST_NEW_DESTINATION_NAME = "Destination B";
	public static String TEST_NEW_DESTINATION_URL = "file:///tmp/destb";

	public static long TEST_EXISTING_DESTINATION_ID = 2013;
	public static String TEST_EXISTING_DESTINATION_NAME = "Destination A";
	public static String TEST_EXISTING_DESTINATION_URL = "file:///tmp/desta";

	public static String TEST_INVALID_URL = "/tmp/invalid";
	public static long TEST_NON_EXISTING_DESTINATION_ID = 10000;

	@BeforeClass
	public static void setup() {
		app = fakeApplication(inMemoryDatabase());
		start(app);
		createTestDestinationDirectory();
	}

	@AfterClass
	public static void teardown() {
		stop(app);
		destroyTestDestinationDirectory();
	}

	/**
	 * Create test destination directory (used for rule-file generation tests etc)
	 */
	public static void createTestDestinationDirectory() {
		try {
			Files.createDirectory(Paths.get(TEST_DESTINATIONS_DIR));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Destroy test destination directory (to remove all artifacts produced by test execution)
	 */
	public static void destroyTestDestinationDirectory() {
		try {
			FileUtils.deleteDirectory(new File(TEST_DESTINATIONS_DIR));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
