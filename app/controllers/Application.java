package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(views.html.dashboard.index.render("Welcome to the gateway management console."));
    }

}
