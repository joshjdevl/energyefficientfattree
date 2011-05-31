/**
 *
 */
package edu.ucla.cloud.switches;

import java.util.HashSet;
import java.util.Set;

import edu.ucla.cloud.Server;
import edu.ucla.cloud.switches.model.SwitchConstants;

/**
 * @author Josh
 * 
 */
public class EdgeSwitch extends Switch {

	public EdgeSwitch(final int capacity) {
		super("ES", capacity);
	}

	private final Set<Server> servers = new HashSet<Server>();

	/**
	 * @return the servers
	 */
	public Set<Server> getServers() {
		return servers;
	}

	@Override
	protected int linkCapacity() {
		return SwitchConstants.EDGE_THROUGHPUT;
	}

}
