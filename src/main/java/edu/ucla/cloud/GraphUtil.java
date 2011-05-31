/**
 *
 */
package edu.ucla.cloud;

import java.util.List;

import edu.ucla.cloud.switches.AggregrateSwitch;
import edu.ucla.cloud.switches.CoreSwitch;
import edu.ucla.cloud.switches.EdgeSwitch;

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

	public void resetSwitchCountCapacity(
			final List<CoreSwitch> coreSwitchesGlobal) {
		for (final CoreSwitch coreSwitch : coreSwitchesGlobal) {
			coreSwitch.reset();
			for (final AggregrateSwitch aggregrateSwitch : coreSwitch
					.getAggregrateSwitchs()) {
				aggregrateSwitch.reset();
				for (final EdgeSwitch edgeSwitch : aggregrateSwitch
						.getEdgeSwitchs()) {
					edgeSwitch.reset();
				}
			}
		}
	}
}
