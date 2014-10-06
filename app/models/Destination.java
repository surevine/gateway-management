package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * Gateway destination end-point to share resources with.
 *
 * @author jonnyheavey (jonny.heavey@surevine.com)
 *
 */
@Entity
public class Destination extends Model {

    private static final long serialVersionUID = 1L;

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
