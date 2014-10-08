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
    	Path rule_file_path = Paths.get(destinationsDirectoryPath + "/" + destination.id, ruleFileName);
		Boolean exists = Files.exists(rule_file_path);

		// TODO determine that contents of file match template

		assertThat(exists).isEqualTo(true);

	}

}
