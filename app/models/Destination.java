package models;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.typesafe.config.ConfigFactory;

import play.data.validation.Constraints.Required;
import play.data.validation.ValidationError;
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

    /**
     * Creates directory for destination rule files
     */
    public void createRuleFileDirectory() {

    	String destinationsDirectory = ConfigFactory.load().getString("gateway.destinations.dir");

    	Path destinationsDirectoryPath = Paths.get(destinationsDirectory + "/" + this.id);

    	if(!Files.exists(destinationsDirectoryPath)) {
    		try {
    			Files.createDirectory(destinationsDirectoryPath);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}

    }

    /**
     * Creates rule file on disk for destination
     *
     * @param name Name of file to create
     */
    public void createRuleFile(String fileName) {

    	String destinationsDirectory = ConfigFactory.load().getString("gateway.destinations.dir");
    	String templateRuleFile = ConfigFactory.load().getString("gateway.template.rule.file");

    	Path templateRuleFilePath = Paths.get(templateRuleFile);
    	Path destinationRuleFilePath = Paths.get(destinationsDirectory + "/" + this.id + "/" + fileName);

    	try {
			Files.copy(templateRuleFilePath, destinationRuleFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    /**
     * Perform additional validation (beyond annotations) on model properties.
     *
     * @return List of validation error messages associated with relevant fields (if any exist)
     */
    public List<ValidationError> validate() {

    	List<ValidationError> errors = new ArrayList<ValidationError>();

    	// Validate URL
    	final URL u;
    	try {
    		u = new URL(url);
    	}
    	catch(MalformedURLException e) {
    		errors.add(new ValidationError("url", "Valid URL required."));
    	}

    	return errors.isEmpty() ? null : errors;

    }

}
