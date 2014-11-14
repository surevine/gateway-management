package controllers;

import play.mvc.*;

public class Application extends AuditedController {

	//@Security.Authenticated(WildflyRemoteAuthenticator.class)
    public Result index() {
        return ok(views.html.dashboard.index.render("Welcome to the gateway management console."));
    }

}
