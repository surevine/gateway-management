package com.surevine.gateway.rules;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.typesafe.config.ConfigFactory;

/**
 * Handles management of gateway rule files
 *
 * @author jonnyheavey
 *
 */
public class RuleFileManager {

	public static final String RULES_DIRECTORY = ConfigFactory.load().getString("gateway.rules.dir");
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
