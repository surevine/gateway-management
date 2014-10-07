package models;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Gateway destination end-point to share resources with.
 *
 * @author jonnyheavey (jonny.heavey@surevine.com)
 *
 */
@Entity
public class Destination extends Model {

    private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@Required
	public String name;

	@Required
	public String url;

    /**
     * Generic query helper for entity Computer with id Long
     */
    public static Finder<Long,Destination> find = new Finder<Long,Destination>(Long.class, Destination.class);

    /**
     * Loads rules for the destination from expected rule-file locations
     *
     * @return String contents of rule file
     * @throws IOException
     */
    public String loadRules() throws IOException {

    	String destinationsPath = ConfigFactory.load().getString("gateway.destinations.dir");

    	Path rule_file_path = Paths.get(destinationsPath + "/" + this.id, "custom.js");
		List<String> lines = Files.readAllLines(rule_file_path, Charset.forName("UTF-8"));

    	StringBuffer parsedJsFile = new StringBuffer();

		for (String line : lines) {
			parsedJsFile.append(line + System.lineSeparator());
		}

		return parsedJsFile.toString();

    }

}
