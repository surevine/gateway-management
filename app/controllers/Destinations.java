package controllers;

import java.util.List;

import models.Destination;
import play.*;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.*;

import views.html.*;

public class Destinations extends Controller {

    public static Result list() {

    	List<Destination> destinations = Destination.find.all();

        return ok(views.html.destinations.list.render(destinations));

    }

    public static Result view(Long id) {

    	Destination destination = Destination.find.byId(id);

    	return ok(views.html.destinations.view.render(destination));

    }

    public static Result add() {

    	Form<Destination> destinationForm = Form.form(Destination.class);

    	return ok(views.html.destinations.add.render(destinationForm));

    }

    public static Result edit(Long id) {

    	Destination destination = Destination.find.byId(id);
    	Form<Destination> destinationForm = Form.form(Destination.class).fill(destination);

    	return ok(views.html.destinations.edit.render(destination.id, destinationForm));

    }

    public static Result create() {

    	Form<Destination> destinationForm = Form.form(Destination.class).bindFromRequest();

    	// TODO error checking etc

    	destinationForm.get().save();

    	// TODO add flash
    	//flash("success", "Destination created successfully.");

    	return redirect(routes.Destinations.view(destinationForm.get().id));

    }

    public static Result update(Long id) {

    	Form<Destination> destinationForm = Form.form(Destination.class).bindFromRequest();

    	// TODO validation

    	destinationForm.get().update(id);

    	// TODO add flash
    	//flash("success", "Destination updated successfully.");

    	return redirect(routes.Destinations.view(id));

    }

    public static Result delete(Long id) {

    	Destination destination = Destination.find.byId(id);
    	destination.delete();

    	return redirect(routes.Destinations.list());

    }

}
