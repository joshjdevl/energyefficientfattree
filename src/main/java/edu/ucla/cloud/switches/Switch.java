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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((switchId == null) ? 0 : switchId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Switch other = (Switch) obj;
		if (switchId == null) {
			if (other.switchId != null)
				return false;
		} else if (!switchId.equals(other.switchId))
			return false;
		return true;
	}

}
