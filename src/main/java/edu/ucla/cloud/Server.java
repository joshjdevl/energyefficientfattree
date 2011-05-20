package edu.ucla.cloud;

public class Server implements Node {
	private final String serverId;
	private boolean active = true;

	public Server(final String edgeSwitchId, final int serverNumber) {
		super();
		this.serverId = edgeSwitchId + "-" + serverNumber;
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
