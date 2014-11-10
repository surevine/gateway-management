package com.surevine.gateway.rules;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import models.Destination;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import play.test.FakeApplication;

import com.typesafe.config.ConfigFactory;

import destinations.DestinationTest;

public class RuleFileManagerTest {

	private static final String TEST_ORIGINAL_EXPORT_RULE_FILE_CONTENTS = "existing export rules";
	private static final String TEST_ORIGINAL_IMPORT_RULE_FILE_CONTENTS = "existing import rules";

	public static FakeApplication app;

	private RuleFileManager fixture = RuleFileManager.getInstance();

	@BeforeClass
	public static void setup() {
		app = fakeApplication(inMemoryDatabase());
		start(app);

		createTestDirectories();
		createTestFiles();
	}

	@AfterClass
	public static void teardown() {
		stop(app);
		destroyTestDirectories();
	}

	@Test
	public void testCreateDestinationRuleFileDirectory() {
		Destination destination = new Destination(DestinationTest.TEST_EXISTING_DESTINATION_ID,
													DestinationTest.TEST_EXISTING_DESTINATION_NAME,
													DestinationTest.TEST_EXISTING_DESTINATION_URL);

		fixture.createDestinationRuleFileDirectory(destination);

		Path destinationDirPath = Paths.get(DestinationTest.TEST_DESTINATIONS_DIR + "/" +
												DestinationTest.TEST_EXISTING_DESTINATION_ID);
		assertThat(Files.exists(destinationDirPath)).isEqualTo(true);
	}

	@Test
	public void testCreateDestinationRuleFile() {
		Destination destination = new Destination(DestinationTest.TEST_EXISTING_DESTINATION_ID,
													DestinationTest.TEST_EXISTING_DESTINATION_NAME,
													DestinationTest.TEST_EXISTING_DESTINATION_URL);

		fixture.createDestinationRuleFile(destination, Destination.DEFAULT_EXPORT_RULEFILE_NAME);

		Path destinationRuleFilePath = Paths.get(DestinationTest.TEST_DESTINATIONS_DIR + "/" + DestinationTest.TEST_EXISTING_DESTINATION_ID,
													Destination.DEFAULT_EXPORT_RULEFILE_NAME);

		assertThat(Files.exists(destinationRuleFilePath)).isEqualTo(true);

		Path templateRuleFilePath = Paths.get(ConfigFactory.load().getString("gateway.template.rule.file"));
		try {
			String parsedTemplate = loadFileContents(templateRuleFilePath);
			String parsedDestinationRuleFile = loadFileContents(destinationRuleFilePath);
			assertThat(parsedTemplate).isEqualTo(parsedDestinationRuleFile);
		}
		catch(IOException e) {
			fail("Error loading file contents: " + e.getMessage());
		}
	}

	@Test
	public void testDeleteDestinationRuleFileDirectory() {
		Destination destination = new Destination(DestinationTest.TEST_EXISTING_DESTINATION_ID,
													DestinationTest.TEST_EXISTING_DESTINATION_NAME,
													DestinationTest.TEST_EXISTING_DESTINATION_URL);

		fixture.deleteDestinationRuleFileDirectory(destination);

		Path destinationDirPath = Paths.get(DestinationTest.TEST_DESTINATIONS_DIR + "/" + DestinationTest.TEST_EXISTING_DESTINATION_ID);
		assertThat(Files.exists(destinationDirPath)).isEqualTo(false);
	}

	@Test
	public void testLoadGlobalExportRules() {

		Path globalExportRuleFilePath = Paths.get(RuleFileManager.RULES_DIRECTORY, RuleFileManager.DEFAULT_GLOBAL_EXPORT_RULEFILE_NAME);

		String expectedRuleFileContent = "";
		try {
			expectedRuleFileContent = loadFileContents(globalExportRuleFilePath);
		} catch (IOException e) {
			fail("Could not load global export rule file for contents comparison");
		}

		String globalRuleFileContent = "";
		try {
			globalRuleFileContent = fixture.loadGlobalExportRules();
		} catch (IOException e) {
			fail("Failed to load global export rule file contents");
		}

		assertThat(globalRuleFileContent).isEqualTo(expectedRuleFileContent);

	}

	@Test
	public void testLoadGlobalImportRules() {

		Path globalImportRuleFilePath = Paths.get(RuleFileManager.RULES_DIRECTORY, RuleFileManager.DEFAULT_GLOBAL_IMPORT_RULEFILE_NAME);

		String expectedRuleFileContent = "";
		try {
			expectedRuleFileContent = loadFileContents(globalImportRuleFilePath);
		} catch (IOException e) {
			fail("Could not load global import rule file for contents comparison");
		}

		String globalRuleFileContent = "";
		try {
			globalRuleFileContent = fixture.loadGlobalImportRules();
		} catch (IOException e) {
			fail("Failed to load global import rule file contents");
		}

		assertThat(globalRuleFileContent).isEqualTo(expectedRuleFileContent);

	}

	@Test
	public void testLoadDestinationExportRules() {

		Destination destination = new Destination(DestinationTest.TEST_EXISTING_DESTINATION_ID,
													DestinationTest.TEST_EXISTING_DESTINATION_NAME,
													DestinationTest.TEST_EXISTING_DESTINATION_URL);
		destination.save();

		Path destinationRuleFileTemplatePath = Paths.get(RuleFileManager.DESTINATION_TEMPLATE_RULE_FILE);

		String expectedRuleFileContent = "";
		try {

			expectedRuleFileContent = loadFileContents(destinationRuleFileTemplatePath);
		} catch (IOException e) {
			fail("Could not load destination rule file template for contents comparison");
		}

		String destinationRuleFileContent = "";
		try {
			destinationRuleFileContent = fixture.loadDestinationExportRules(destination);
		} catch (IOException e) {
			fail("Failed to load destination rule file contents");
		}

		assertThat(destinationRuleFileContent).isEqualTo(expectedRuleFileContent);
	}

	@Test
	@Ignore
	public void testUpdateDestinationExportRules() {
		// TODO
	}

	@Test
	@Ignore
	public void testUpdateGlobalExportRules() {
		// TODO
	}

	@Test
	@Ignore
	public void testUpdateGlobalImportRules() {
		// TODO
	}

	/**
	 * Helper method to read file contents
	 * @param filePath path of file to read
	 * @return String of file contents
	 * @throws IOException
	 */
	private String loadFileContents(Path filePath) throws IOException {
		List<String> fileLines = Files.readAllLines(filePath, Charset.forName("UTF-8"));
    	StringBuffer parsedFile = new StringBuffer();
		for (String line : fileLines) {
			parsedFile.append(line + System.lineSeparator());
		}
		return parsedFile.toString();
	}

	private static void createTestDirectories() {
		try {
			Files.createDirectory(Paths.get(RuleFileManager.RULES_DIRECTORY));
			Files.createDirectory(Paths.get(RuleFileManager.DESTINATIONS_RULES_DIRECTORY));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createTestFiles() {
		Path globalExportRuleFilePath = Paths.get(RuleFileManager.RULES_DIRECTORY, RuleFileManager.DEFAULT_GLOBAL_EXPORT_RULEFILE_NAME);
		Path globalImportRuleFilePath = Paths.get(RuleFileManager.RULES_DIRECTORY, RuleFileManager.DEFAULT_GLOBAL_IMPORT_RULEFILE_NAME);

		try {
			Files.write(globalExportRuleFilePath, TEST_ORIGINAL_EXPORT_RULE_FILE_CONTENTS.getBytes());
			Files.write(globalImportRuleFilePath, TEST_ORIGINAL_IMPORT_RULE_FILE_CONTENTS.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void destroyTestDirectories() {
		try {
			FileUtils.deleteDirectory(new File(RuleFileManager.RULES_DIRECTORY));
			FileUtils.deleteDirectory(new File(RuleFileManager.DESTINATIONS_RULES_DIRECTORY));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
