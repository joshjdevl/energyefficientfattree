/**
 *
 */
package edu.ucla.cloud;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.ucla.cloud.switches.Switch;

/**
 * @author Josh
 * 
 */
public class GraphUtil {

	public void removeAllServerEdges(final List<Server> serversGlobal) {
		for (final Server server : serversGlobal) {
			server.setActive(false);
		}
	}

	public void resetToActiveLinks() {

	}

	public void resetSwitchCountCapacity(
			final Map<String, Switch> switchGlobalList) {
		for (final Entry<String, Switch> cur : switchGlobalList.entrySet()) {
			final Switch node = cur.getValue();
			node.reset();
		}

	}
}
