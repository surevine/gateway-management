package destinations;

import static org.fest.assertions.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import models.Destination;

import org.junit.Test;

/**
 * Tests for the models.Destination class functionality
 *
 * @author jonnyheavey
 *
 */
public class DestinationModelTest extends DestinationTest {

	@Test
	public void testCreateRuleDirectory() {

		Destination destination = new Destination();
		destination.id = TEST_NEW_DESTINATION_ID;

		destination.createRuleFileDirectory();

		Path destinationDirPath = Paths.get(TEST_DESTINATIONS_DIR + "/" + destination.id);

		assertThat(Files.exists(destinationDirPath)).isEqualTo(true);
	}

	@Test
	public void testCreateRuleFile() {

		Destination destination = new Destination();
		destination.id = TEST_NEW_DESTINATION_ID;

		destination.createRuleFileDirectory();
		destination.createRuleFile(Destination.DEFAULT_RULEFILE_NAME);

		// Determine whether rule file exists
    	Path rule_file_path = Paths.get(TEST_DESTINATIONS_DIR + "/" + destination.id, Destination.DEFAULT_RULEFILE_NAME);
		Boolean exists = Files.exists(rule_file_path);

		// TODO determine that contents of file match template

		assertThat(exists).isEqualTo(true);
	}

}
