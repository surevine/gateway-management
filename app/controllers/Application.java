package controllers;

import play.mvc.*;

public class Application extends Controller {

	@Security.Authenticated(WildflyRemoteAuthenticator.class)
    public static Result index() {
        return ok(views.html.dashboard.index.render("Welcome to the gateway management console."));
    }

}
