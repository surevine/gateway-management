package destinations;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import play.test.FakeApplication;

import com.typesafe.config.ConfigFactory;

/**
 * Common test setup / config for Destination tests.
 * Used by other destination test classes.
 *
 * @author jonnyheavey
 *
 */
public class DestinationTest {

	public static FakeApplication app;

	public static String TEST_DESTINATIONS_DIR = ConfigFactory.load().getString("gateway.destinations.dir");
	public static long TEST_DESTINATION_ID = 2014;

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
