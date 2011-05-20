package edu.ucla.cloud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.ucla.cloud.switches.AggregrateSwitch;
import edu.ucla.cloud.switches.CoreSwitch;
import edu.ucla.cloud.switches.EdgeSwitch;

/**
 * Represents top level tree defined with the help of Core level switches and
 * pods Servers are hidden inside edge switches
 * 
 */
public class ExtElasticTree {

	private final Set<CoreSwitch> coreSwitches = new HashSet<CoreSwitch>();
	private final Set<Pod> pods = new HashSet<Pod>();

	private final int aggregateSwitchesPerPod = 2;
	private final int edgeSwitchesPerPod = 2;
	private final int serversPerEdgeSwitch = 48;
	private final int numberOfCoreSwitches = 4;

	public ExtElasticTree() {
		this(4);
	}

	public ExtElasticTree(final int numPods) {
		for (int p = 0; p < numPods; p++) {
			final Pod pod = createPod(p);
			pods.add(pod);
		}
	}

	private Pod createPod(final int num) {
		final Pod pod = new Pod(num);
		final Set<EdgeSwitch> edgeSwitchs = createEdgeSwitches();

		for (int as = 0; as < aggregateSwitchesPerPod; as++) {
			final AggregrateSwitch aggregrateSwitch = new AggregrateSwitch();
			aggregrateSwitch.getEdgeSwitchs().addAll(edgeSwitchs);
		}
		return pod;
	}

	private Set<EdgeSwitch> createEdgeSwitches() {
		final Set<EdgeSwitch> edgeSwitchs = new HashSet<EdgeSwitch>();
		for (int es = 0; es < edgeSwitchesPerPod; es++) {
			final EdgeSwitch edgeSwitch = new EdgeSwitch();
			final Set<Server> servers = createServers(edgeSwitch.getSwitchId());
			edgeSwitch.getServers().addAll(servers);
			edgeSwitchs.add(edgeSwitch);
		}
		return edgeSwitchs;
	}

	private Set<Server> createServers(final String edgeSwitchId) {
		final Set<Server> servers = new HashSet<Server>();
		for (int s = 0; s < serversPerEdgeSwitch; s++) {
			final Server server = new Server(edgeSwitchId, s);
			servers.add(server);
		}
		return servers;
	}

	public Map<String, Node> compute() {

		return new HashMap<String, Node>();
	}
}
