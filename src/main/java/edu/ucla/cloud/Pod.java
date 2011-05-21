package edu.ucla.cloud;

import java.util.ArrayList;
import java.util.List;

import edu.ucla.cloud.switches.AggregrateSwitch;

/**
 * Represents Pod containing two level switch hierarchy
 * 
 */
public class Pod {

	private final int podId;

	private final List<AggregrateSwitch> aggregrateSwitchs = new ArrayList<AggregrateSwitch>();

	public Pod(final int podId) {
		this.podId = podId;
	}

	/**
	 * @return the aggregrateSwitchs
	 */
	public List<AggregrateSwitch> getAggregrateSwitchs() {
		return aggregrateSwitchs;
	}

}
