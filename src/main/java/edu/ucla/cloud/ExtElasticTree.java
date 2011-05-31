package edu.ucla.cloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.mxgraph.analysis.mxGraphAnalysis;
import com.mxgraph.analysis.mxICostFunction;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxCellState;
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
	private Display display = null;

	private final GraphUtil graphUtil = new GraphUtil();

	private final List<CoreSwitch> coreSwitchesGlobal = new ArrayList<CoreSwitch>();
	private final Set<Pod> pods = new HashSet<Pod>();
	private final List<Server> serversGlobal = new ArrayList<Server>();
	private final List<EdgeSwitch> edgeSwitchesGlobal = new ArrayList<EdgeSwitch>();
	private final List<AggregrateSwitch> aggregrateSwitchesGlobal = new ArrayList<AggregrateSwitch>();

	private final int numberOfPods;
	private final int aggregateSwitchesPerPod;
	private final int edgeSwitchesPerPod;
	private final int serversPerEdgeSwitch;
	private final int numberOfCoreSwitches;

	private final int UNIT_OF_WORK_PER_SERVER = 2;

	Map<String, Switch> switchMasterList = new HashMap<String, Switch>();
	final Map<CoreSwitch, mxCell> coreNodes = new HashMap<CoreSwitch, mxCell>();
	final Map<AggregrateSwitch, mxCell> aggregateNodes = new HashMap<AggregrateSwitch, mxCell>();
	final Map<EdgeSwitch, mxCell> edgeNodes = new HashMap<EdgeSwitch, mxCell>();
	final Map<Server, mxCell> serverNodes = new HashMap<Server, mxCell>();

	public ExtElasticTree() {
		this(2, 2, 2, 2, 2);
		// this(2, 2, 2, 2, 2);
	}

	public ExtElasticTree(final int numPods, final int aggregateSwitchesPerPod,
			final int edgeSwitchesPerPod, final int serversPerEdgeSwitch,
			final int numberOfCoreSwitches) {
		this.numberOfCoreSwitches = numberOfCoreSwitches;
		this.serversPerEdgeSwitch = serversPerEdgeSwitch;
		this.edgeSwitchesPerPod = edgeSwitchesPerPod;
		this.aggregateSwitchesPerPod = aggregateSwitchesPerPod;
		numberOfPods = numPods;

		final int totalServers = numberOfPods * edgeSwitchesPerPod
				* serversPerEdgeSwitch;

		for (int c = 0; c < numberOfCoreSwitches; c++) {
			final CoreSwitch coreSwitch = new CoreSwitch(totalServers / 2);
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
						aggregrateSwitch.getCoreSwitches().add(coreSwitch);
					}
				} else {
					for (int c = numberOfCoreSwitches / 2; c < numberOfCoreSwitches; c++) {
						final CoreSwitch coreSwitch = coreSwitchesGlobal.get(c);
						coreSwitch.getAggregrateSwitchs().add(aggregrateSwitch);
						aggregrateSwitch.getCoreSwitches().add(coreSwitch);
					}
				}
			}
		}
	}

	public int calculateRoutingThroughput() {

		return 0;
	}

	public void clearServerEdges() {
		graphUtil.removeAllServerEdges(serversGlobal);
	}

	private mxGraph getGraph() {

		final mxGraph graph = new mxGraph();
		final Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();

		int x = 180;
		for (final CoreSwitch coreSwitch : coreSwitchesGlobal) {
			coreSwitch.reset();
			final mxCell node = (mxCell) graph.insertVertex(parent, null,
					coreSwitch.getSwitchId(), x, 20, 80, 30);
			x += 250;
			coreNodes.put(coreSwitch, node);
			switchMasterList.put((String) node.getValue(), coreSwitch);
		}
		x = 80;
		for (final AggregrateSwitch aggregrateSwitch : aggregrateSwitchesGlobal) {
			aggregrateSwitch.reset();
			final mxCell node = (mxCell) graph.insertVertex(parent, null,
					aggregrateSwitch.getSwitchId(), x, 150, 80, 30);
			x += 150;
			aggregateNodes.put(aggregrateSwitch, node);
			switchMasterList.put((String) node.getValue(), aggregrateSwitch);
		}

		x = 80;
		for (final EdgeSwitch edgeSwitch : edgeSwitchesGlobal) {
			edgeSwitch.reset();
			final mxCell node = (mxCell) graph.insertVertex(parent, null,
					edgeSwitch.getSwitchId(), x, 250, 80, 30);
			x += 150;
			edgeNodes.put(edgeSwitch, node);
			switchMasterList.put((String) node.getValue(), edgeSwitch);
		}
		x = 10;
		int count = 0;
		int y = 350;
		for (final Server server : serversGlobal) {
			count++;
			if (count % 10 == 0) {
				y += 100;
				x = 10;
			}
			final mxCell node = (mxCell) graph.insertVertex(parent, null,
					server.getServerId(), x, y, 50, 10);
			x += 140;
			serverNodes.put(server, node);

		}

		for (final CoreSwitch coreSwitch : coreSwitchesGlobal) {

			final mxCell cNode = coreNodes.get(coreSwitch);

			for (final AggregrateSwitch aggregrateSwitch : coreSwitch
					.getAggregrateSwitchs()) {
				final mxCell aNode = aggregateNodes.get(aggregrateSwitch);
				if (aggregrateSwitch.isActive() && coreSwitch.isActive()) {
					graph.insertEdge(parent, null, coreSwitch.getSwitchId()
							+ "+" + aggregrateSwitch.getSwitchId(), cNode,
							aNode);
				}
				// graph.insertEdge(parent, null, aggregrateSwitch
				// .getSwitchId()
				// + coreSwitch.getSwitchId(), aNode, cNode);

				for (final EdgeSwitch edgeSwitch : aggregrateSwitch
						.getEdgeSwitchs()) {
					final mxCell eNode = edgeNodes.get(edgeSwitch);
					if (edgeSwitch.isActive() && aggregrateSwitch.isActive()) {

						graph.insertEdge(parent, null, aggregrateSwitch
								.getSwitchId()
								+ "+" + edgeSwitch.getSwitchId(), aNode, eNode);
					}
					// graph.insertEdge(parent, null, edgeSwitch
					// .getSwitchId()
					// + aggregrateSwitch.getSwitchId(), eNode,
					// aNode);

					for (final Server server : edgeSwitch.getServers()) {
						if (server.isActive() && edgeSwitch.isActive()) {
							final mxCell sNode = serverNodes.get(server);
							graph.insertEdge(parent, null, edgeSwitch
									.getSwitchId()
									+ "+" + server.getServerId(), eNode, sNode);
							// graph.insertEdge(parent, null, server
							// .getServerId()
							// + edgeSwitch.getSwitchId(), sNode,
							// eNode);
						}
					}

				}

			}

		}

		graph.getModel().endUpdate();

		return graph;
	}

	public double bruteForce(final int numberOfSwitchesToEliminate) {
		getGraph();

		double cost = Double.MIN_VALUE;

		final Set<Entry<String, Switch>> entrySet = switchMasterList.entrySet();

		for (final Entry<String, Switch> cur : entrySet) {
			final Switch node = cur.getValue();
			node.setActive(false);
			final double runningCost = bruteForceRecurse(entrySet, 1,
					numberOfSwitchesToEliminate, cost);
			if (runningCost > cost) {
				cost = runningCost;
				display = new Display(print());
			}
			node.setActive(true);
		}

		display.activate();

		return cost;
	}

	private double bruteForceRecurse(final Set<Entry<String, Switch>> entrySet,
			final int currentDepth, final int numberOfSwitchesToEliminate,
			final double threshold) {

		final double highestCost = threshold;

		if (currentDepth >= numberOfSwitchesToEliminate) {
			final double topologyCost = topologyThroughput();
			if (topologyCost == 908.0383116883118) {
				System.out.println("found");

			}
			return topologyCost;
		}

		for (final Entry<String, Switch> cur : entrySet) {
			final Switch node = cur.getValue();
			if (node.isActive()) {
				node.setActive(false);
				double iterationRunningCost = bruteForceRecurse(entrySet,
						currentDepth + 1, numberOfSwitchesToEliminate,
						threshold);
				node.setActive(true);
				if (iterationRunningCost > highestCost) {
					iterationRunningCost = highestCost;
				}
			}
		}

		return highestCost;
	}

	public double topologyThroughput() {
		final mxGraph graph = getGraph();

		double effectiveThroughput = 0.0;

		final mxICostFunction cf = new mxICostFunction() {
			@Override
			public double getCost(final mxCellState state) {

				final mxCell node = (mxCell) state.getCell();
				final String key = node.getValue().toString().split("\\+")[0];

				final Switch switchNode = switchMasterList.get(key);
				final double throughput = switchNode.effectiveThroughputCost();
				// System.out.println(key + "," + throughput);
				return throughput;

			}

		};

		final mxGraphAnalysis mga = mxGraphAnalysis.getInstance();

		final Set<EdgeSwitch> edgesToCheck = new HashSet<EdgeSwitch>();
		edgesToCheck.addAll(edgeSwitchesGlobal);

		Iterator<EdgeSwitch> edgeIter = edgesToCheck.iterator();
		// edgeIter.next();
		// edgeIter.remove();

		for (final EdgeSwitch edgeSwitch : edgeSwitchesGlobal) {

			while (edgeIter.hasNext()) {

				final EdgeSwitch compare = edgeIter.next();
				// System.out.println(compare.getSwitchId());

				final Iterator<Server> serverIter1 = edgeSwitch.getServers()
						.iterator();

				while (serverIter1.hasNext()) {
					final Server server1 = serverIter1.next();
					final Iterator<Server> serverIter2 = compare.getServers()
							.iterator();
					while (serverIter2.hasNext()) {

						Server server2 = serverIter2.next();
						while (server1.equals(server2) && serverIter2.hasNext()) {
							// System.out.println(server1.getServerId() + ","
							// + server2.getServerId());
							server2 = serverIter2.next();
						}

						final Object from = serverNodes.get(server1);
						final Object to = serverNodes.get(server2);

						final Iterator<Entry<EdgeSwitch, mxCell>> i = edgeNodes
								.entrySet().iterator();

						final Object[] edges = mga.getShortestPath(graph, from,
								to, cf, 9999, false);
						if (edges == null || edges.length == 0) {

							// return Double.MIN_VALUE;
						} else {
							// System.out.print("Length=" + edges.length + "|");
							for (final Object cur : edges) {
								final mxCell c = (mxCell) cur;
								final String key = c.getValue().toString()
										.split("\\+")[0];
								final Switch switchNode = switchMasterList
										.get(key);
								if (switchNode != null) {
									switchNode.incrementCount();
									effectiveThroughput += switchNode.getCost();
									if (switchNode instanceof CoreSwitch) {
										// System.out
										// .println(switchNode.getCost());
									}

									// System.out.println(switchNode.getSwitchId()
									// + "=" + switchNode.getCost());
									// if (effectiveThroughput > 2000) {
									// System.out.println(server1.getServerId()
									// + "," + server2.getServerId());
									//
									// System.out.println(effectiveThroughput);
									// }
								} else {
									// System.out.println(key);
								}

								// System.out.print(c.getValue() + "|");
							}
							// System.out.println("");
						}

					}

				}
			}
			// edgeIter.remove();
			edgesToCheck.remove(edgeSwitch);
			edgeIter = edgesToCheck.iterator();
		}

		// System.out.println(effectiveThroughput);

		return effectiveThroughput;
	}

	public mxGraphComponent print() {
		final mxGraph graph = getGraph();

		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		return graphComponent;
	}

	public int calculateThroughput() {
		final int CORE_THROUGHPUT = 1000;
		final int AGGREGATE_THROUGHPUT = 100;
		final int EDGE_THROUGHPUT = 10;

		int throughputSum = 0;

		for (final CoreSwitch coreSwitch : coreSwitchesGlobal) {
			if (coreSwitch.isActive()) {
				for (final AggregrateSwitch aggregrateSwitch : coreSwitch
						.getAggregrateSwitchs()) {
					if (aggregrateSwitch.isActive()) {
						throughputSum += CORE_THROUGHPUT;
						for (final EdgeSwitch edgeSwitch : aggregrateSwitch
								.getEdgeSwitchs()) {
							if (edgeSwitch.isActive()) {
								throughputSum += AGGREGATE_THROUGHPUT;
								for (final Server server : edgeSwitch
										.getServers()) {
									if (server.isActive()) {
										throughputSum += EDGE_THROUGHPUT;
									}
								}
							}
						}
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
			final AggregrateSwitch aggregrateSwitch = new AggregrateSwitch(
					serversPerEdgeSwitch);
			aggregrateSwitch.getEdgeSwitchs().addAll(edgeSwitchs);
			pod.getAggregrateSwitchs().add(aggregrateSwitch);
			aggregrateSwitchesGlobal.add(aggregrateSwitch);
		}
		return pod;
	}

	private Set<EdgeSwitch> createEdgeSwitches() {
		final Set<EdgeSwitch> edgeSwitchs = new HashSet<EdgeSwitch>();
		for (int es = 0; es < edgeSwitchesPerPod; es++) {
			final EdgeSwitch edgeSwitch = new EdgeSwitch(serversPerEdgeSwitch);
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
			final Server server = new Server(edgeSwitchId, s);
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
