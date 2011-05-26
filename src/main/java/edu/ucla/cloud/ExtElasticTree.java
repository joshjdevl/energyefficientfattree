package edu.ucla.cloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import edu.ucla.cloud.switches.AggregrateSwitch;
import edu.ucla.cloud.switches.CoreSwitch;
import edu.ucla.cloud.switches.EdgeSwitch;
import edu.ucla.cloud.switches.Switch;

/**
 * Represents top level tree defined with the help of Core level switches and
 * pods Servers are hidden inside edge switches
 * 
 */
public class ExtElasticTree {

	private final List<CoreSwitch> coreSwitchesGlobal = new ArrayList<CoreSwitch>();
	private final Set<Pod> pods = new HashSet<Pod>();
	private final List<Server> serversGlobal = new ArrayList<Server>();
	private final List<EdgeSwitch> edgeSwitchesGlobal = new ArrayList<EdgeSwitch>();
	private final List<AggregrateSwitch> aggregrateSwitchesGlobal = new ArrayList<AggregrateSwitch>();

	private final int aggregateSwitchesPerPod = 4;
	private final int edgeSwitchesPerPod = 2;
	private final int serversPerEdgeSwitch = 48;
	private final int numberOfCoreSwitches = 4;

	private final int UNIT_OF_WORK_PER_SERVER = 2;

	public ExtElasticTree() {
		this(4);
	}

	public ExtElasticTree(final int numPods) {
		for (int c = 0; c < numberOfCoreSwitches; c++) {
			final CoreSwitch coreSwitch = new CoreSwitch();
			coreSwitchesGlobal.add(coreSwitch);
		}

		for (int p = 0; p < numPods; p++) {
			final Pod pod = createPod(p);
			pods.add(pod);

			for (int a = 0; a < pod.getAggregrateSwitchs().size(); a++) {
				final AggregrateSwitch aggregrateSwitch = pod
						.getAggregrateSwitchs().get(a);
				if (a % 2 == 0) {
					for (int c = 0; c < numberOfCoreSwitches / 2; c++) {
						final CoreSwitch coreSwitch = coreSwitchesGlobal.get(c);
						coreSwitch.getAggregrateSwitchs().add(aggregrateSwitch);
					}
				} else {
					for (int c = numberOfCoreSwitches / 2; c < numberOfCoreSwitches; c++) {
						final CoreSwitch coreSwitch = coreSwitchesGlobal.get(c);
						coreSwitch.getAggregrateSwitchs().add(aggregrateSwitch);
					}
				}
			}
		}
	}

	public mxGraphComponent print() {
		final mxGraph graph = new mxGraph();
		final Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();

		final Map<CoreSwitch, Object> coreNodes = new HashMap<CoreSwitch, Object>();
		final Map<AggregrateSwitch, Object> aggregateNodes = new HashMap<AggregrateSwitch, Object>();
		final Map<EdgeSwitch, Object> edgeNodes = new HashMap<EdgeSwitch, Object>();
		Map<Server,Object> serverNodes = new HashMap<Server, Object>();
		
		
		int x = 180;
		for (final CoreSwitch coreSwitch : coreSwitchesGlobal) {
			final Object node = graph.insertVertex(parent, null, coreSwitch
					.getSwitchId(), x, 20, 80, 30);
			x += 250;
			coreNodes.put(coreSwitch, node);
		}
		x = 80;
		for (final AggregrateSwitch aggregrateSwitch : aggregrateSwitchesGlobal) {
			final Object node = graph.insertVertex(parent, null,
					aggregrateSwitch.getSwitchId(), x, 150, 80, 30);
			x += 150;
			aggregateNodes.put(aggregrateSwitch, node);
		}

		x = 80;
		for (final EdgeSwitch edgeSwitch : edgeSwitchesGlobal) {
			final Object node = graph.insertVertex(parent, null, edgeSwitch
					.getSwitchId(), x, 250, 80, 30);
			x += 150;
			edgeNodes.put(edgeSwitch, node);
		}
		x = 10;
		int count = 0;
		int y = 350;
		for(Server server : serversGlobal) {
			count++;
			if(count % 15 == 0) {
				y+=100;
				x=10;
			}
			final Object node = graph.insertVertex(parent, null, server
					.getServerId(), x, y, 50, 10);
			x += 90;
			serverNodes.put(server, node);
		
		}
		
		for (final CoreSwitch coreSwitch : coreSwitchesGlobal) {
			final Object cNode = coreNodes.get(coreSwitch);

			for (final AggregrateSwitch aggregrateSwitch : coreSwitch
					.getAggregrateSwitchs()) {
				final Object aNode = aggregateNodes.get(aggregrateSwitch);
				if( aggregrateSwitch.isActive()) {
					graph.insertEdge(parent, null, "", cNode, aNode);

					for (final EdgeSwitch edgeSwitch : aggregrateSwitch
							.getEdgeSwitchs()) {
						if( edgeSwitch.isActive()) {
							final Object eNode = edgeNodes.get(edgeSwitch);
							graph.insertEdge(parent, null, "", aNode, eNode);
							
							for(Server server : edgeSwitch.getServers()) {
								if(server.isActive()) {
									final Object sNode = serverNodes.get(server);
									graph.insertEdge(parent, null, "d", eNode, sNode);									
								}
							}
						}

					}					
				}

			}

		}

		graph.getModel().endUpdate();

		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		return graphComponent;
	}

	public int calculateThroughput() {
		final int CORE_THROUGHPUT = 1000;
		final int AGGREGATE_THROUGHPUT = 100;
		final int EDGE_THROUGHPUT = 10;

		int throughputSum = 0;

		for (final CoreSwitch coreSwitch : coreSwitchesGlobal) {
			for (final AggregrateSwitch aggregrateSwitch : coreSwitch
					.getAggregrateSwitchs()) {
				throughputSum += CORE_THROUGHPUT;
				for (final EdgeSwitch edgeSwitch : aggregrateSwitch
						.getEdgeSwitchs()) {
					throughputSum += AGGREGATE_THROUGHPUT;
					for (final Server server : edgeSwitch.getServers()) {
						throughputSum += EDGE_THROUGHPUT;
					}
				}
			}
		}
		return throughputSum;
	}

	private Pod createPod(final int num) {
		final Pod pod = new Pod(num);
		final Set<EdgeSwitch> edgeSwitchs = createEdgeSwitches();

		for (int as = 0; as < aggregateSwitchesPerPod; as++) {
			final AggregrateSwitch aggregrateSwitch = new AggregrateSwitch();
			aggregrateSwitch.getEdgeSwitchs().addAll(edgeSwitchs);
			pod.getAggregrateSwitchs().add(aggregrateSwitch);
			aggregrateSwitchesGlobal.add(aggregrateSwitch);
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
			edgeSwitchesGlobal.add(edgeSwitch);
		}
		return edgeSwitchs;
	}

	private Set<Server> createServers(final String edgeSwitchId) {
		final Set<Server> servers = new HashSet<Server>();
		for (int s = 0; s < serversPerEdgeSwitch; s++) {
			final Server server = new Server("Server", s);
			servers.add(server);
			serversGlobal.add(server);
		}
		return servers;
	}

	public Map<String, Node> compute(final int targetTotalWork) {
		int currentWork = 0;

		for (final Server server : serversGlobal) {
			if (currentWork < targetTotalWork) {
				currentWork += UNIT_OF_WORK_PER_SERVER;
				server.setActive(true);
			} else {
				// System.out.println("Setting server to inactive. "
				// + server.getServerId());
				server.setActive(false);
			}
		}

		for (final EdgeSwitch edgeSwitch : edgeSwitchesGlobal) {
			boolean disableEdge = true;
			for (final Server server : edgeSwitch.getServers()) {
				if (server.isActive()) {
					disableEdge = false;
					break;
				}
			}
			if (disableEdge) {
				// System.out.println("Setting edgeswitch to inactive."
				// + edgeSwitch.getSwitchId());
				edgeSwitch.setActive(false);
			}
		}

		for (final AggregrateSwitch aggregrateSwitch : aggregrateSwitchesGlobal) {
			boolean disableAggregate = true;
			for (final EdgeSwitch edgeSwitch : aggregrateSwitch
					.getEdgeSwitchs()) {
				if (edgeSwitch.isActive()) {
					disableAggregate = false;
					break;
				}
			}
			if (disableAggregate) {
				// System.out.println("Setting aggregateswitch to inactive."
				// + aggregrateSwitch.getSwitchId());
				aggregrateSwitch.setActive(false);
			}
		}

		for (final CoreSwitch coreSwitch : coreSwitchesGlobal) {
			boolean disableCore = true;
			for (final AggregrateSwitch aggregrateSwitch : coreSwitch
					.getAggregrateSwitchs()) {
				if (aggregrateSwitch.isActive()) {
					disableCore = false;
					break;
				}
			}
			if (disableCore) {
				// System.out.println("Setting coreswitch to inactive."
				// + coreSwitch.getSwitchId());
				coreSwitch.setActive(false);
			}
		}

		return new HashMap<String, Node>();
	}

	public void disableSwitch(final List<? extends Switch> switches) {

	}
}
