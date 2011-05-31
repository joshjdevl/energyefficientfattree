/**
 *
 */
package edu.ucla.cloud.switches;

import java.util.HashSet;
import java.util.Set;

import edu.ucla.cloud.switches.model.SwitchConstants;

/**
 * @author Josh
 * 
 */
public class AggregrateSwitch extends Switch {

	public AggregrateSwitch(final int capacity) {
		super("AggregateSwitch", capacity);

	}

	private final Set<EdgeSwitch> edgeSwitchs = new HashSet<EdgeSwitch>();
	private final Set<CoreSwitch> coreSwitches = new HashSet<CoreSwitch>();

	/**
	 * @return the edgeSwitchs
	 */
	public Set<EdgeSwitch> getEdgeSwitchs() {
		return edgeSwitchs;
	}

	/**
	 * @return the coreSwitches
	 */
	public Set<CoreSwitch> getCoreSwitches() {
		return coreSwitches;
	}

	@Override
	protected int linkCapacity() {

		return SwitchConstants.AGGREGATE_THROUGHPUT;
	}

}
