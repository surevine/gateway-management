package models;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.typesafe.config.ConfigFactory;

import play.test.FakeApplication;

public class DestinationTest {

	public static FakeApplication app;

	// TODO load from alternative test configuration
	public static String DESTINATIONS_DIR = ConfigFactory.load().getString("gateway.destinations.dir");
	public static long TEST_DESTINATION_ID = 2014;

	@BeforeClass
	public static void startApp() {
		app = fakeApplication(inMemoryDatabase());
		start(app);
	}

	@AfterClass
	public static void stopApp() {
		stop(app);

		// Delete all artifacts (files/directories) produced by tests in this class
		try {
			FileUtils.deleteDirectory(new File(DESTINATIONS_DIR + "/" + TEST_DESTINATION_ID));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void testCreateRuleDirectory() {

		Destination destination = new Destination();
		destination.id = TEST_DESTINATION_ID;

		destination.createRuleFileDirectory();

		Path destinationDirPath = Paths.get(DESTINATIONS_DIR + "/" + destination.id);

		assertThat(Files.exists(destinationDirPath)).isEqualTo(true);
	}

	@Test
	@Ignore
	public void testCreateRuleFile() {

		String ruleFileName = "custom.js";

		Destination destination = new Destination();
		destination.id = TEST_DESTINATION_ID;

		destination.createRuleFileDirectory();
		destination.createRuleFile(ruleFileName);

		// Determine whether rule file exists
    	Path rule_file_path = Paths.get(DESTINATIONS_DIR + "/" + destination.id, ruleFileName);
		Boolean exists = Files.exists(rule_file_path);

		// TODO determine that contents of file match template

		assertThat(exists).isEqualTo(true);
	}

}
