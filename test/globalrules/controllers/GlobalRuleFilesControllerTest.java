package globalrules.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.status;
import static play.test.Helpers.stop;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.surevine.gateway.rules.RuleFileManager;

import destinations.DestinationTest;

import play.Logger;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import specs2.files;

public class GlobalRuleFilesControllerTest {

	public static FakeApplication app;

	private static final String TEST_ORIGINAL_EXPORT_RULE_FILE_CONTENTS = "existing export rules";
	private static final String TEST_ORIGINAL_IMPORT_RULE_FILE_CONTENTS = "existing import rules";
	private static final String TEST_UPDATED_RULE_FILE_CONTENTS = "test js rule file contents";

	@BeforeClass
	public static void setup() {

		app = fakeApplication(inMemoryDatabase());
		start(app);

		createTestGlobalRulesDir();
		createTestGlobalRuleFiles();

	}

	@AfterClass
	public static void teardown() {
		stop(app);
		destroyTestGlobalRulesDir();
	}

	@Test
	public void testViewGlobalRules() {
		FakeRequest request = new FakeRequest(GET, "/global-rules");
		Result result = callAction(controllers.routes.ref.GlobalRuleFiles.view(), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains("Import");
		assertThat(content).contains("Export");
	}

	@Test
	public void testEditGlobalExportRules() {
		FakeRequest request = new FakeRequest(GET, "/global-rules/edit/export");
		Result result = callAction(controllers.routes.ref.GlobalRuleFiles.edit("export"), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains("global-export.js");
	}

	@Test
	public void testEditGlobalImportRules() {
		FakeRequest request = new FakeRequest(GET, "/global-rules/edit/import");
		Result result = callAction(controllers.routes.ref.GlobalRuleFiles.edit("import"), request);

		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");

		String content = contentAsString(result);
		assertThat(content).contains("global-import.js");
	}

	@Test
	public void testEditNonExistingExportRules() {
		FakeRequest request = new FakeRequest(GET, "/global-rules/edit/badslug");
		Result result = callAction(controllers.routes.ref.GlobalRuleFiles.edit("badslug"), request);

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}

	@Test
	public void testUpdateGlobalExportRules() {
		Result result = postUpdateGlobalDestinationRuleFiles("export");

		assertThat(status(result)).isEqualTo(SEE_OTHER);

		String content;
		try {
			content = RuleFileManager.getInstance().loadGlobalExportRules();
			assertThat(content).contains(TEST_UPDATED_RULE_FILE_CONTENTS);
		} catch (IOException e) {
			fail();
		}

	}

	@Test
	public void testUpdateGlobalImportRules() {
		Result result = postUpdateGlobalDestinationRuleFiles("import");

		assertThat(status(result)).isEqualTo(SEE_OTHER);

		String content;
		try {
			content = RuleFileManager.getInstance().loadGlobalImportRules();
			assertThat(content).contains(TEST_UPDATED_RULE_FILE_CONTENTS);
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public void testUpdateNonExistingExportRules() {
		Result result = postUpdateGlobalDestinationRuleFiles("badslug");

		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}

	/**
	 * Helper method to post to update global rule file action
	 * @param slug URL slug representing file to update
	 * @return Result of request (response from server)
	 */
	private Result postUpdateGlobalDestinationRuleFiles(String slug) {
		Map<String,String> formData = new HashMap<String,String>();
		formData.put("ruleFileContent", TEST_UPDATED_RULE_FILE_CONTENTS);

		FakeRequest request = new FakeRequest(POST, "/global-rules/edit/" + slug);
		Result result = callAction(controllers.routes.ref.GlobalRuleFiles.update(slug), request.withFormUrlEncodedBody(formData));

		return result;
	}

	private static void createTestGlobalRulesDir() {
		try {
			Files.createDirectory(Paths.get(RuleFileManager.RULES_DIRECTORY));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createTestGlobalRuleFiles() {
		Path globalExportRuleFilePath = Paths.get(RuleFileManager.RULES_DIRECTORY, RuleFileManager.DEFAULT_GLOBAL_EXPORT_RULEFILE_NAME);
		Path globalImportRuleFilePath = Paths.get(RuleFileManager.RULES_DIRECTORY, RuleFileManager.DEFAULT_GLOBAL_IMPORT_RULEFILE_NAME);

		try {
			Files.write(globalExportRuleFilePath, TEST_ORIGINAL_EXPORT_RULE_FILE_CONTENTS.getBytes());
			Files.write(globalImportRuleFilePath, TEST_ORIGINAL_IMPORT_RULE_FILE_CONTENTS.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void destroyTestGlobalRulesDir() {
		try {
			FileUtils.deleteDirectory(new File(RuleFileManager.RULES_DIRECTORY));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
