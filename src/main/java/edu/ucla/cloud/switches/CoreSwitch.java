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
public class CoreSwitch extends Switch {

	public CoreSwitch(final int capacity) {
		super("CoreSwitch", capacity);
		// TODO Auto-generated constructor stub
	}

	private final Set<AggregrateSwitch> aggregrateSwitchs = new HashSet<AggregrateSwitch>();

	/**
	 * @return the aggregrateSwitchs
	 */
	public Set<AggregrateSwitch> getAggregrateSwitchs() {
		return aggregrateSwitchs;
	}

	@Override
	protected int linkCapacity() {
		return SwitchConstants.CORE_THROUGHPUT;
	}

}
