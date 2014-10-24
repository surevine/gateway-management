package com.surevine.gateway.rules;

import static org.fest.assertions.Assertions.assertThat;

import java.nio.file.Files;

import models.Destination;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import destinations.DestinationTest;

public class RuleFileManagerTest extends DestinationTest {

	/**
	 * Create existing destination in database (for use by delete tests)
	 */
	@BeforeClass
	public static void createExistingTestDestination() {
		Destination destination = new Destination(TEST_EXISTING_DESTINATION_ID, TEST_EXISTING_DESTINATION_NAME, TEST_EXISTING_DESTINATION_URL);
		destination.save();
	}

	@Test
	@Ignore
	public static void testCreateDestinationRuleFile() {
		// TODO
		//Boolean exists = Files.exists(rule_file_path);
		//assertThat(exists).isEqualTo(true);
	}

	@Test
	@Ignore
	public static void testCreateDestinationRuleFileDirectory() {
		// TODO
		//assertThat(Files.exists(destinationDirPath)).isEqualTo(true);
	}

	@Test
	@Ignore
	public static void testDeleteDestinationRuleFileDirectory() {
		// TODO
		//Boolean exists = Files.exists(destinationDirPath);
		//assertThat(exists).isEqualTo(false);
	}

}
