package models;

import java.io.File;
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

import org.apache.commons.io.FileUtils;

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
	 * Configured location of destinations rule file directories
	 */
	public static final String DESTINATIONS_RULES_DIRECTORY = ConfigFactory.load().getString("gateway.destinations.dir");

	/**
	 * Default name of rule file generated when destination created
	 */
	public static final String DEFAULT_RULEFILE_NAME = "custom.js";

    /**
     * Generic query helper for entity Computer with id Long
     */
    public static Finder<Long,Destination> find = new Finder<Long,Destination>(Long.class, Destination.class);

    public Destination(long id, String name, String url) {
    	this.id = id;
    	this.name = name;
    	this.url = url;
    }

    public Destination(String name, String url) {
    	this.name = name;
    	this.url = url;
    }

    @Override
    public void save() {
    	super.save();
    	createRuleFileDirectory();
    	createRuleFile(DEFAULT_RULEFILE_NAME);
    }

    @Override
    public void delete() {
    	deleteRuleFileDirectory();
    	super.delete();
    }

	/**
     * Loads rules for the destination from expected rule-file locations
     *
     * @return String contents of rule file
     * @throws IOException
     */
    public String loadRules() throws IOException {

    	Path rule_file_path = Paths.get(DESTINATIONS_RULES_DIRECTORY + "/" + this.id, DEFAULT_RULEFILE_NAME);
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

    	Path destinationsDirectoryPath = Paths.get(DESTINATIONS_RULES_DIRECTORY + "/" + this.id);

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

    	String templateRuleFile = ConfigFactory.load().getString("gateway.template.rule.file");

    	Path templateRuleFilePath = Paths.get(templateRuleFile);
    	Path destinationRuleFilePath = Paths.get(DESTINATIONS_RULES_DIRECTORY + "/" + this.id + "/" + fileName);

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
     * @return List of validation error messages associated with relevant properties
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

    /**
     * Delete this destinations rule file directory
     */
    private void deleteRuleFileDirectory() {
    	try {
			FileUtils.deleteDirectory(new File(DESTINATIONS_RULES_DIRECTORY + "/" + this.id));
		} catch (IOException e1) {
			e1.printStackTrace();
			// TODO log
		}
    }
}
