package com.surevine.gateway.rules;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;

import play.Logger;

import models.Destination;

import com.typesafe.config.ConfigFactory;

/**
 * Handles management of gateway rule files
 *
 * @author jonnyheavey
 *
 */
public class RuleFileManager {

	public static final String RULES_DIRECTORY = ConfigFactory.load().getString("gateway.rules.dir");
	public static final String DESTINATIONS_RULES_DIRECTORY = ConfigFactory.load().getString("gateway.destinations.rules.dir");

	public static final String DEFAULT_EXPORT_RULEFILE_NAME = "export.js";

	public static final String DEFAULT_GLOBAL_EXPORT_RULEFILE_NAME = "global-export.js";
	public static final String DEFAULT_GLOBAL_IMPORT_RULEFILE_NAME = "global-import.js";

	private static RuleFileManager _instance = null;

	private RuleFileManager() {

	}

	/**
	 * Get an instance of RuleFileManager
	 * @return the RuleFileManager
	 */
	public static RuleFileManager getInstance() {
		if(_instance == null) {
			_instance = new RuleFileManager();
		}
		return _instance;
	}

	/**
	 * Creates a new rule file based on configured template
	 * @param destination
	 * @param fileName
	 */
	public void createDestinationRuleFile(Destination destination, String fileName) {
    	String templateRuleFile = ConfigFactory.load().getString("gateway.template.rule.file");

    	Path templateRuleFilePath = Paths.get(templateRuleFile);
    	Path destinationRuleFilePath = Paths.get(DESTINATIONS_RULES_DIRECTORY + "/" + destination.id + "/" + fileName);

    	try {
			Files.copy(templateRuleFilePath, destinationRuleFilePath);
		} catch (IOException e) {
			Logger.error("Failed to create rule file for destination: "+ destination.name, e);
		}
	}

	/**
	 * Update a destinations rule file
	 * @param destination
	 * @param ruleFileContent
	 * @throws IOException
	 */
	public void updateDestinationRuleFile(Destination destination, String ruleFileContent) throws IOException {
		Path destinationRuleFilePath = Paths.get(DESTINATIONS_RULES_DIRECTORY + "/" + destination.id + "/" + DEFAULT_EXPORT_RULEFILE_NAME);
		Files.write(destinationRuleFilePath, ruleFileContent.getBytes());
	}

	/**
	 * Creates directory on disk for destination rules
	 * @param destination
	 */
	public void createDestinationRuleFileDirectory(Destination destination) {
		Path destinationsDirectoryPath = Paths.get(DESTINATIONS_RULES_DIRECTORY + "/" + destination.id);

    	if(!Files.exists(destinationsDirectoryPath)) {
    		try {
    			Files.createDirectory(destinationsDirectoryPath);
    		} catch (IOException e) {
    			Logger.error("Failed to create rule file directory for destination: " + destination.name, e);
    		}
    	}
	}

	/**
	 * Delete a destinations rule file directory
	 * @param destination destinations directory to delete
	 */
	public void deleteDestinationRuleFileDirectory(Destination destination) {
    	try {
    		Path destinationsDirectoryPath = Paths.get(DESTINATIONS_RULES_DIRECTORY + "/" + destination.id).toAbsolutePath();
			FileUtils.deleteDirectory(destinationsDirectoryPath.toFile());
		} catch (IOException e) {
			Logger.warn("Failed to delete rule file directory for destination: " + destination.id, e);
		}
	}

	/**
	 * Loads export rule file for destination
	 * @return contents of rule file
	 * @throws IOException
	 */
	public String loadDestinationExportRules(Destination destination) throws IOException {
		Path destinationExportRuleFilePath = Paths.get(DESTINATIONS_RULES_DIRECTORY + "/" + destination.id, DEFAULT_EXPORT_RULEFILE_NAME);
		return readRuleFile(destinationExportRuleFilePath);
	}

	/**
	 * Load global export rule file
	 * @return the contents of the rule file
	 * @throws IOException
	 */
	public String loadGlobalExportRules() throws IOException {
		Path exportRuleFilePath = Paths.get(RULES_DIRECTORY, DEFAULT_GLOBAL_EXPORT_RULEFILE_NAME);
		return readRuleFile(exportRuleFilePath);
	}

	/**
	 * Load global import rule file
	 * @return the contents of the rule file
	 * @throws IOException
	 */
	public String loadGlobalImportRules() throws IOException {
		Path importRuleFilePath = Paths.get(RULES_DIRECTORY, DEFAULT_GLOBAL_IMPORT_RULEFILE_NAME);
		return readRuleFile(importRuleFilePath);
	}

	/**
	 * Update the global export rule file
	 * @param ruleFileContent new contents of rule file
	 * @throws IOException
	 */
	public void updateGlobalExportRules(String ruleFileContent) throws IOException {
		Path globalExportRuleFilePath = Paths.get(RULES_DIRECTORY + "/"+ DEFAULT_GLOBAL_EXPORT_RULEFILE_NAME);
		Files.write(globalExportRuleFilePath, ruleFileContent.getBytes());
	}

	/**
	 * Update the global export rule file
	 * @param ruleFileContent new contents of rule file
	 * @throws IOException
	 */
	public void updateGlobalImportRules(String ruleFileContent) throws IOException {
		Path globalExportRuleFilePath = Paths.get(RULES_DIRECTORY + "/"+ DEFAULT_GLOBAL_IMPORT_RULEFILE_NAME);
		Files.write(globalExportRuleFilePath, ruleFileContent.getBytes());
	}

	/**
	 * Reads a rule file from disk.
	 * @param ruleFilePath Path of file to read
	 * @return String of file contents
	 * @throws IOException
	 */
	private String readRuleFile(Path ruleFilePath) throws IOException {
		List<String> lines = Files.readAllLines(ruleFilePath, Charset.forName("UTF-8"));
    	StringBuffer parsedJsFile = new StringBuffer();

		for (String line : lines) {
			parsedJsFile.append(line + System.lineSeparator());
		}

		return parsedJsFile.toString();
	}

}
