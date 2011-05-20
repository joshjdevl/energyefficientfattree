/**
 *
 */
package edu.ucla.cloud.switches;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Josh
 * 
 */
public class CoreSwitch extends Switch {

	public CoreSwitch() {
		super("CoreSwitch");
		// TODO Auto-generated constructor stub
	}

	private final Set<AggregrateSwitch> aggregrateSwitchs = new HashSet<AggregrateSwitch>();

	/**
	 * @return the aggregrateSwitchs
	 */
	public Set<AggregrateSwitch> getAggregrateSwitchs() {
		return aggregrateSwitchs;
	}

}
