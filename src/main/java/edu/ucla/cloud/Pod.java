package edu.ucla.cloud;

import java.util.HashSet;
import java.util.Set;

import edu.ucla.cloud.switches.AggregrateSwitch;

/**
 * Represents Pod containing two level switch hierarchy
 * 
 */
public class Pod {

	private final int podId;

	private final Set<AggregrateSwitch> aggregrateSwitchs = new HashSet<AggregrateSwitch>();

	public Pod(final int podId) {
		this.podId = podId;
	}

	/**
	 * @return the aggregrateSwitchs
	 */
	public Set<AggregrateSwitch> getAggregrateSwitchs() {
		return aggregrateSwitchs;
	}

}