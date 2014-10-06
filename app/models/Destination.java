package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Destination extends Model {

	@Id
	public Long id;

	public String name;

	public String url;


    /**
     * Generic query helper for entity Computer with id Long
     */
    public static Finder<Long,Destination> find = new Finder<Long,Destination>(Long.class, Destination.class);

}
