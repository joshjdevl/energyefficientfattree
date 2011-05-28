package edu.ucla.cloud;

public class Server implements Node {
	private final String serverId;
	private boolean active = true;

	public Server(final String edgeSwitchId, final int serverNumber) {
		this.serverId = "Server-" + serverNumber;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serverId == null) ? 0 : serverId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Server other = (Server) obj;
		if (serverId == null) {
			if (other.serverId != null)
				return false;
		} else if (!serverId.equals(other.serverId))
			return false;
		return true;
	}

	/**
	 * @return the serverId
	 */
	public String getServerId() {
		return serverId;
	}

}
