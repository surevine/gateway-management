package controllers;

import models.Partner;
import models.Repository;
import play.mvc.*;

public class Application extends AuditedController {

	@Security.Authenticated(AppAuthenticator.class)
    public Result index() {

		int partnerCount = Partner.FIND.all().size();
		int repositoryCount = Repository.FIND.all().size();

        return ok(views.html.dashboard.index.render("Welcome to the gateway management console.", partnerCount, repositoryCount));
    }

}
