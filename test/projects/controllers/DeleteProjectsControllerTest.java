package projects.controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;

import models.OutboundProject;

import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Result;
import play.test.FakeRequest;
import projects.ProjectTest;

/**
 * Tests for the project controller delete functionality (routes/actions)
 *
 * @author jonnyheavey
 *
 */
public class DeleteProjectsControllerTest extends ProjectTest {

	/**
	 * Create existing project in database (for use by delete tests)
	 */
	@BeforeClass
	public static void createExistingTestProject() {
		OutboundProject project = new OutboundProject(TEST_EXISTING_PROJECT_ID,
										TEST_EXISTING_PROJECT_DISPLAY_NAME,
										TEST_EXISTING_PROJECT_PROJECT_KEY,
										TEST_EXISTING_PROJECT_SLUG_REPO);
		project.save();
	}

	@Test
	public void testDeleteProject() {
		Result result = postDeleteProject(TEST_EXISTING_PROJECT_ID);

		// Expect 303 as implementation redirects to 'list' page
		assertThat(status(result)).isEqualTo(SEE_OTHER);

		OutboundProject project = OutboundProject.FIND.byId(TEST_EXISTING_PROJECT_ID);
		assertThat(project).isNull();
	}

	@Test
	public void testDeleteNonExistingProject() {
		Result result = postDeleteProject(TEST_NON_EXISTING_PROJECT_ID);

		assertThat(status(result)).isEqualTo(NOT_FOUND);
	}

	/**
	 * Helper method for fake posting to delete project route
	 * @param id Id of project to delete
	 * @return Result Response from server
	 */
	private Result postDeleteProject(long id) {

		FakeRequest request = new FakeRequest(POST, "/projects/delete/" + id);
		Result result = callAction(controllers.routes.ref.OutboundProjects.delete(id), request);

		return result;
	}

}
