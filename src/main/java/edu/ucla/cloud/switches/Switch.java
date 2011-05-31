package edu.ucla.cloud.switches;

import edu.ucla.cloud.Node;

public abstract class Switch implements Node {

	private int count = 0;
	private final int capacity;
	private boolean active = true;
	private final String switchId;
	static int ID_GENERATOR = 1;

	public Switch(final String prefix, final int capacity) {
		this.switchId = prefix + "-" + ID_GENERATOR++;
		this.capacity = capacity;
	}

	public void incrementCount() {
		count++;
	}

	public void reset() {
		count = 0;
	}

	protected void decrementCount() {
		count--;
	}

	public double getCost() {
		final double cost = fractionCost() * linkCapacity();
		return cost;
	}

	public double effectiveThroughputCost() {
		incrementCount();

		final double cost = shortestPathFractionCost();

		decrementCount();

		return cost;
	}

	abstract protected double linkCapacity();

	public double shortestPathFractionCost() {

		if (count <= capacity) {
			return 1.0;
		} else {

			final double ratio = (double) count / (double) capacity;
			// System.out.println(capacity + "," + count + "," + ratio);
			return ratio;
		}

	}

	public double fractionCost() {
		if (count <= capacity) {
			return 1.0;
		} else {

			final double ratio = (double) capacity / (double) count;
			// System.out.println(capacity + "," + count + "," + ratio);
			return ratio;
		}
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
