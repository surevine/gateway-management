package controllers;

import play.mvc.*;

@org.springframework.stereotype.Controller
public class Application extends AuditedController {

	//@Security.Authenticated(RemoteAuthenticator.class)
    public Result index() {
        return ok(views.html.dashboard.index.render("Welcome to the gateway management console."));
    }

}
