/**
 *
 */
package edu.ucla.cloud.switches;

import java.util.HashSet;
import java.util.Set;

import edu.ucla.cloud.Server;

/**
 * @author Josh
 * 
 */
public class EdgeSwitch extends Switch {
	public EdgeSwitch() {
		super("EdgeSwitch");
	}

	private final Set<Server> servers = new HashSet<Server>();

	/**
	 * @return the servers
	 */
	public Set<Server> getServers() {
		return servers;
	}

}
