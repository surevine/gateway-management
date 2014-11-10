package com.surevine.gateway.rules;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import models.Destination;

import org.junit.Ignore;
import org.junit.Test;

import play.Logger;

import com.typesafe.config.ConfigFactory;

import destinations.DestinationTest;

public class RuleFileManagerTest extends DestinationTest {

	private RuleFileManager fixture = RuleFileManager.getInstance();

	@Test
	public void testCreateDestinationRuleFileDirectory() {
		Destination destination = new Destination(TEST_EXISTING_DESTINATION_ID, TEST_EXISTING_DESTINATION_NAME, TEST_EXISTING_DESTINATION_URL);

		fixture.createDestinationRuleFileDirectory(destination);

		Path destinationDirPath = Paths.get(TEST_DESTINATIONS_DIR + "/" + TEST_EXISTING_DESTINATION_ID);
		assertThat(Files.exists(destinationDirPath)).isEqualTo(true);
	}

	@Test
	public void testCreateDestinationRuleFile() {
		Destination destination = new Destination(TEST_EXISTING_DESTINATION_ID, TEST_EXISTING_DESTINATION_NAME, TEST_EXISTING_DESTINATION_URL);

		fixture.createDestinationRuleFile(destination, Destination.DEFAULT_EXPORT_RULEFILE_NAME);

		Path destinationRuleFilePath = Paths.get(TEST_DESTINATIONS_DIR + "/" + TEST_EXISTING_DESTINATION_ID, Destination.DEFAULT_EXPORT_RULEFILE_NAME);
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
		Destination destination = new Destination(TEST_EXISTING_DESTINATION_ID, TEST_EXISTING_DESTINATION_NAME, TEST_EXISTING_DESTINATION_URL);

		fixture.deleteDestinationRuleFileDirectory(destination);

		Path destinationDirPath = Paths.get(TEST_DESTINATIONS_DIR + "/" + TEST_EXISTING_DESTINATION_ID);
		assertThat(Files.exists(destinationDirPath)).isEqualTo(false);
	}

	@Test
	public void testLoadGlobalExportRules() {

		Path globalExportRuleFilePath = Paths.get(ConfigFactory.load().getString("gateway.rules.dir"), RuleFileManager.DEFAULT_GLOBAL_EXPORT_RULEFILE_NAME);

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

		Path globalImportRuleFilePath = Paths.get(ConfigFactory.load().getString("gateway.rules.dir"), RuleFileManager.DEFAULT_GLOBAL_IMPORT_RULEFILE_NAME);

		Logger.error("***JONNY" + globalImportRuleFilePath.toString());

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
	@Ignore
	public void testLoadDestinationExportRules() {
		// TODO
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

}
