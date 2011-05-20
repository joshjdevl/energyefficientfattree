package edu.ucla.cloud;

import java.util.Map;

public class Switch implements Node {

	/**
	 * String because it will allow to specify level where that switch is present: c-core, a-aggregation, e-edge
	 * So, switchId will be c0- 0th switch at core level, a2- 2nd aggregation switch etc
	 */
	private String switchId;
	private Map<String, Node> links;
	static int ID_GENERATOR = 1;
	
	public Switch(String level) {
		this.switchId = level + ID_GENERATOR++;
	}
}
