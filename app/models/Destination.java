package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Destination extends Model {

	@Id
	public Long id;

	@Required
	public String name;

	@Required
	public String url;


    /**
     * Generic query helper for entity Computer with id Long
     */
    public static Finder<Long,Destination> find = new Finder<Long,Destination>(Long.class, Destination.class);

}
