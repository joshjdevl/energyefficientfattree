package edu.ucla.cloud;

import java.util.Map;
import java.util.Set;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExtElasticTree eetree = new ExtElasticTree();
		Map<String, Node> result = eetree.compute();
	}

}
