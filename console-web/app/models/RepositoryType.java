package models;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Repository types supporting gateway federation
 * @author jonnyheavey
 */
public enum RepositoryType {

	SCM("Source code management"),
	ISSUE("Issue tracking");

	public final String name;

	RepositoryType(String name) {
		this.name = name;
	}

	/**
	 * Provides values in format expected by Play template engine
	 * to populate form controls.
	 * @return
	 */
	public static Map<String, String> options(){
        Map<String, String> vals = new LinkedHashMap<String, String>();
        for (RepositoryType rType : RepositoryType.values()) {
            vals.put(rType.toString(), rType.name);
        }
        return vals;
    }

}
