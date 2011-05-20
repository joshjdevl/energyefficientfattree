package edu.ucla.cloud;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents top level tree defined with the help of Core level switches and pods
 * Servers are hidden inside edge switches
 *
 */
public class ExtElasticTree {

	private int numPods;
	private Map<String, Node> coreSwitches;
	private Map<Integer, Set<Node>> pods;
	
	public ExtElasticTree() {
		this(4);
	}
	
	public ExtElasticTree(int numPods) {
		this.numPods = numPods;
	}
	
	public Map<String, Node> compute() {
		
		return new HashMap<String, Node>();
	}
}
