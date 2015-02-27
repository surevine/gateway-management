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

import models.Partner;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.FakeApplication;

import com.typesafe.config.ConfigFactory;

import destinations.DestinationTest;

public class RuleFileManagerTest {

	private static final String TEST_ORIGINAL_EXPORT_RULE_FILE_CONTENTS = "existing export rules";
	private static final String TEST_ORIGINAL_IMPORT_RULE_FILE_CONTENTS = "existing import rules";
	private static final String TEST_UPDATED_RULE_FILE_CONTENT = "updated rule file";

	public static FakeApplication app;

	private RuleFileManager fixture = RuleFileManager.getInstance();

	@Before
	public void beforeTest() {
		app = fakeApplication(inMemoryDatabase());
		start(app);

		createTestDirectories();
		createTestArtifacts();
	}

	@After
	public void afterTest() {
		destroyTestDirectories();

		stop(app);
	}

	@Test
	public void testCreateDestinationRuleFileDirectory() {
		Partner destination = new Partner(DestinationTest.TEST_EXISTING_DESTINATION_ID,
													DestinationTest.TEST_EXISTING_DESTINATION_NAME,
													DestinationTest.TEST_EXISTING_DESTINATION_URL);

		fixture.createPartnerRuleFileDirectory(destination);

		Path destinationDirPath = Paths.get(DestinationTest.TEST_DESTINATIONS_DIR + "/" +
												DestinationTest.TEST_EXISTING_DESTINATION_ID);
		assertThat(Files.exists(destinationDirPath)).isEqualTo(true);
	}

	@Test
	public void testCreateDestinationRuleFile() {
		Partner destination = new Partner(DestinationTest.TEST_EXISTING_DESTINATION_ID,
													DestinationTest.TEST_EXISTING_DESTINATION_NAME,
													DestinationTest.TEST_EXISTING_DESTINATION_URL);

		fixture.createPartnerRuleFile(destination, Partner.DEFAULT_EXPORT_RULEFILE_NAME);

		Path destinationRuleFilePath = Paths.get(DestinationTest.TEST_DESTINATIONS_DIR + "/" + DestinationTest.TEST_EXISTING_DESTINATION_ID,
													Partner.DEFAULT_EXPORT_RULEFILE_NAME);

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
		Partner destination = new Partner(DestinationTest.TEST_EXISTING_DESTINATION_ID,
													DestinationTest.TEST_EXISTING_DESTINATION_NAME,
													DestinationTest.TEST_EXISTING_DESTINATION_URL);

		fixture.deletePartnerRuleFileDirectory(destination);

		Path destinationDirPath = Paths.get(DestinationTest.TEST_DESTINATIONS_DIR + "/" + DestinationTest.TEST_EXISTING_DESTINATION_ID);
		assertThat(Files.exists(destinationDirPath)).isEqualTo(false);
	}

	@Test
	public void testLoadGlobalExportRules() {

		String expectedRuleFileContent = TEST_ORIGINAL_EXPORT_RULE_FILE_CONTENTS;

		String globalRuleFileContent = "";
		try {
			globalRuleFileContent = fixture.loadGlobalExportRules();
		} catch (IOException e) {
			fail("Failed to load global export rule file contents");
		}

		assertThat(globalRuleFileContent.trim()).isEqualTo(expectedRuleFileContent.trim());

	}

	@Test
	public void testLoadGlobalImportRules() {

		String expectedRuleFileContent = TEST_ORIGINAL_IMPORT_RULE_FILE_CONTENTS;

		String globalRuleFileContent = "";
		try {
			globalRuleFileContent = fixture.loadGlobalImportRules();
		} catch (IOException e) {
			fail("Failed to load global import rule file contents");
		}

		assertThat(globalRuleFileContent.trim()).isEqualTo(expectedRuleFileContent.trim());

	}

	@Test
	public void testLoadDestinationExportRules() {

		Partner destination = Partner.FIND.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);

		Path destinationRuleFileTemplatePath = Paths.get(RuleFileManager.PARTNER_TEMPLATE_RULE_FILE);

		String expectedRuleFileContent = "";
		try {

			expectedRuleFileContent = loadFileContents(destinationRuleFileTemplatePath);
		} catch (IOException e) {
			fail("Could not load destination rule file template for contents comparison");
		}

		String destinationRuleFileContent = "";
		try {
			destinationRuleFileContent = fixture.loadPartnerExportRules(destination);
		} catch (IOException e) {
			fail("Failed to load destination rule file contents");
		}

		assertThat(destinationRuleFileContent).isEqualTo(expectedRuleFileContent);
	}

	@Test
	public void testUpdateDestinationExportRules() {

		Partner destination = Partner.FIND.byId(DestinationTest.TEST_EXISTING_DESTINATION_ID);

		try {
			fixture.updatePartnerRuleFile(destination, TEST_UPDATED_RULE_FILE_CONTENT);
		} catch (IOException e) {
			fail("Failed to update destination rule file");
		}

		Path ruleFilePath = Paths.get(RuleFileManager.PARTNERS_RULES_DIRECTORY + "/" + destination.id,
										RuleFileManager.DEFAULT_EXPORT_RULEFILE_NAME);

		String ruleFileContent = "";
		try {
			ruleFileContent = loadFileContents(ruleFilePath).trim();
		} catch (IOException e) {
			fail("Failed to load rule file for content inspection.");
		}

		assertThat(ruleFileContent).isEqualTo(TEST_UPDATED_RULE_FILE_CONTENT);

	}

	@Test
	public void testUpdateGlobalExportRules() {

		try {
			fixture.updateGlobalExportRules(TEST_UPDATED_RULE_FILE_CONTENT);
		} catch (IOException e) {
			fail("Failed to update global export rule file");
		}

		Path ruleFilePath = Paths.get(RuleFileManager.RULES_DIRECTORY, RuleFileManager.DEFAULT_GLOBAL_EXPORT_RULEFILE_NAME);

		String ruleFileContent = "";
		try {
			ruleFileContent = loadFileContents(ruleFilePath).trim();
		} catch (IOException e) {
			fail("Failed to load rule file for content inspection.");
		}

		assertThat(ruleFileContent).isEqualTo(TEST_UPDATED_RULE_FILE_CONTENT);

	}

	@Test
	public void testUpdateGlobalImportRules() {

		try {
			fixture.updateGlobalImportRules(TEST_UPDATED_RULE_FILE_CONTENT);
		} catch (IOException e) {
			fail("Failed to update global import rule file");
		}

		Path ruleFilePath = Paths.get(RuleFileManager.RULES_DIRECTORY, RuleFileManager.DEFAULT_GLOBAL_IMPORT_RULEFILE_NAME);

		String ruleFileContent = "";
		try {
			ruleFileContent = loadFileContents(ruleFilePath).trim();
		} catch (IOException e) {
			fail("Failed to load rule file for content inspection.");
		}

		assertThat(ruleFileContent).isEqualTo(TEST_UPDATED_RULE_FILE_CONTENT);

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

	private void createTestDirectories() {
		try {
			Files.createDirectory(Paths.get(RuleFileManager.RULES_DIRECTORY));
			Files.createDirectory(Paths.get(RuleFileManager.PARTNERS_RULES_DIRECTORY));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createTestArtifacts() {
		Path globalExportRuleFilePath = Paths.get(RuleFileManager.RULES_DIRECTORY, RuleFileManager.DEFAULT_GLOBAL_EXPORT_RULEFILE_NAME);
		Path globalImportRuleFilePath = Paths.get(RuleFileManager.RULES_DIRECTORY, RuleFileManager.DEFAULT_GLOBAL_IMPORT_RULEFILE_NAME);

		try {
			Files.write(globalExportRuleFilePath, TEST_ORIGINAL_EXPORT_RULE_FILE_CONTENTS.getBytes());
			Files.write(globalImportRuleFilePath, TEST_ORIGINAL_IMPORT_RULE_FILE_CONTENTS.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Partner destination = new Partner(DestinationTest.TEST_EXISTING_DESTINATION_ID,
				DestinationTest.TEST_EXISTING_DESTINATION_NAME,
				DestinationTest.TEST_EXISTING_DESTINATION_URL);
		destination.save();
	}

	private void destroyTestDirectories() {
		try {
			FileUtils.deleteDirectory(new File(RuleFileManager.RULES_DIRECTORY));
			FileUtils.deleteDirectory(new File(RuleFileManager.PARTNERS_RULES_DIRECTORY));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
