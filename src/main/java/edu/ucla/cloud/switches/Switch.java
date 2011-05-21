package edu.ucla.cloud.switches;

import edu.ucla.cloud.Node;

public abstract class Switch implements Node {

	private boolean active = true;
	private final String switchId;
	static int ID_GENERATOR = 1;

	public Switch(final String prefix) {
		this.switchId = prefix + "-" + ID_GENERATOR++;
	}

	/**
	 * @return the switchId
	 */
	public String getSwitchId() {
		return switchId;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

}
