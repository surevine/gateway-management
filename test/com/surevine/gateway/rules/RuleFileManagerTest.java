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

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.typesafe.config.ConfigFactory;

import play.Logger;

import destinations.DestinationTest;

public class RuleFileManagerTest extends DestinationTest {

	@Test
	public void testCreateDestinationRuleFileDirectory() {
		Destination destination = new Destination(TEST_EXISTING_DESTINATION_ID, TEST_EXISTING_DESTINATION_NAME, TEST_EXISTING_DESTINATION_URL);
		RuleFileManager ruleFileManager = RuleFileManager.getInstance();

		ruleFileManager.createDestinationRuleFileDirectory(destination);

		Path destinationDirPath = Paths.get(TEST_DESTINATIONS_DIR + "/" + TEST_EXISTING_DESTINATION_ID);
		assertThat(Files.exists(destinationDirPath)).isEqualTo(true);
	}

	@Test
	public void testCreateDestinationRuleFile() {
		Destination destination = new Destination(TEST_EXISTING_DESTINATION_ID, TEST_EXISTING_DESTINATION_NAME, TEST_EXISTING_DESTINATION_URL);

		RuleFileManager ruleFileManager = RuleFileManager.getInstance();
		ruleFileManager.createDestinationRuleFile(destination, Destination.DEFAULT_EXPORT_RULEFILE_NAME);

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

		RuleFileManager ruleFileManager = RuleFileManager.getInstance();
		ruleFileManager.deleteDestinationRuleFileDirectory(destination);

		Path destinationDirPath = Paths.get(TEST_DESTINATIONS_DIR + "/" + TEST_EXISTING_DESTINATION_ID);
		assertThat(Files.exists(destinationDirPath)).isEqualTo(false);
	}

	@Test
	@Ignore
	public void testLoadGlobalExportRules() {
		// TODO
	}

	@Test
	@Ignore
	public void testLoadGlobalImportRules() {
		// TODO
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
