package controllers;

import models.Destination;
import models.Repository;
import play.mvc.*;

public class Application extends AuditedController {

	@Security.Authenticated(AppAuthenticator.class)
    public Result index() {

		int destinationCount = Destination.FIND.all().size();
		int repositoryCount = Repository.FIND.all().size();

        return ok(views.html.dashboard.index.render("Welcome to the gateway management console.", destinationCount, repositoryCount));
    }

}
