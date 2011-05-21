package edu.ucla.cloud;

import java.util.Map;

public class Main {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final ExtElasticTree eetree = new ExtElasticTree();
		final Map<String, Node> result = eetree.compute(2);
	}

}
