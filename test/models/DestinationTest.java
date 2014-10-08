package models;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.test.FakeApplication;

public class DestinationTest {

	public static FakeApplication app;

	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

	@BeforeClass
	public static void startApp() {
		app = fakeApplication(inMemoryDatabase());
		start(app);
	}

	@AfterClass
	public static void stopApp() {

		// TODO tidy up all created files on disk

		stop(app);
	}

	@Test
	public void testCreateRuleFile() {

		Destination destination = new Destination();
		destination.id = (long) 2014;

		String destinationsDirectoryPath = testFolder.getRoot().getAbsolutePath();
		String ruleFileName = "custom.js";

		destination.createRuleFile(destinationsDirectoryPath, ruleFileName);

		// Determine whether rule file exists
		String destinationsPath = ConfigFactory.load().getString("gatewaydestinationsdir");
    	Path rule_file_path = Paths.get(destinationsPath + "/" + destination.id, ruleFileName);
		Boolean exists = Files.exists(rule_file_path);

		assertThat(exists).isEqualTo(true);

	}

}
