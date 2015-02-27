package com.surevine.gateway.rules;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;

import play.Logger;

import models.Partner;

import com.typesafe.config.ConfigFactory;

/**
 * Handles management of gateway rule files
 *
 * @author jonnyheavey
 *
 */
public class RuleFileManager {

	public static final String RULES_DIRECTORY = ConfigFactory.load().getString("gateway.rules.dir");
	public static final String PARTNERS_RULES_DIRECTORY = ConfigFactory.load().getString("gateway.partners.rules.dir");
	public static final String PARTNER_TEMPLATE_RULE_FILE = ConfigFactory.load().getString("gateway.template.rule.file");

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
	 * @param partner
	 * @param fileName
	 */
	public void createPartnerRuleFile(Partner partner, String fileName) {

    	Path templateRuleFilePath = Paths.get(PARTNER_TEMPLATE_RULE_FILE);
    	Path partnerRuleFilePath = Paths.get(PARTNERS_RULES_DIRECTORY + "/" + partner.id + "/" + fileName);

    	try {
			Files.copy(templateRuleFilePath, partnerRuleFilePath);
		} catch (IOException e) {
			Logger.error("Failed to create rule file for partner: "+ partner.name, e);
		}
	}

	/**
	 * Update a partners rule file
	 * @param partner
	 * @param ruleFileContent
	 * @throws IOException
	 */
	public void updatePartnerRuleFile(Partner partner, String ruleFileContent) throws IOException {
		Path partnerRuleFilePath = Paths.get(PARTNERS_RULES_DIRECTORY + "/" + partner.id + "/" + DEFAULT_EXPORT_RULEFILE_NAME);
		Files.write(partnerRuleFilePath, ruleFileContent.getBytes());
	}

	/**
	 * Creates directory on disk for partner rules
	 * @param partner
	 */
	public void createPartnerRuleFileDirectory(Partner partner) {
		Path partnersDirectoryPath = Paths.get(PARTNERS_RULES_DIRECTORY + "/" + partner.id);

    	if(!Files.exists(partnersDirectoryPath)) {
    		try {
    			Files.createDirectory(partnersDirectoryPath);
    		} catch (IOException e) {
    			Logger.error("Failed to create rule file directory for partner: " + partner.name, e);
    		}
    	}
	}

	/**
	 * Delete a partners rule file directory
	 * @param partner partner's directory to delete
	 */
	public void deletePartnerRuleFileDirectory(Partner partner) {
    	try {
    		Path partnersDirectoryPath = Paths.get(PARTNERS_RULES_DIRECTORY + "/" + partner.id).toAbsolutePath();
			FileUtils.deleteDirectory(partnersDirectoryPath.toFile());
		} catch (IOException e) {
			Logger.warn("Failed to delete rule file directory for partner: " + partner.id, e);
		}
	}

	/**
	 * Loads export rule file for partner
	 * @return contents of rule file
	 * @throws IOException
	 */
	public String loadPartnerExportRules(Partner partner) throws IOException {
		Path partnerExportRuleFilePath = Paths.get(PARTNERS_RULES_DIRECTORY + "/" + partner.id, DEFAULT_EXPORT_RULEFILE_NAME);
		return readRuleFile(partnerExportRuleFilePath);
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
	 * Update the global import rule file
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
		List<String> lines = Files.readAllLines(ruleFilePath, Charset.defaultCharset());
		StringBuilder parsedJsFile = new StringBuilder();

		for (String line : lines) {
			parsedJsFile.append(line + System.getProperty("line.separator"));
		}

		return parsedJsFile.toString();
	}

}
