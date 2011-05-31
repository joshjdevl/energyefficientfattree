package edu.ucla.cloud;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;

public class Main extends JFrame {

	public Main() {
		super("Network Topology");

		final ExtElasticTree eetree = new ExtElasticTree();
		// eetree.compute(2);

		// eetree.clearServerEdges();
		System.out.println("IsConnected=" + eetree.areAllServersConnected());
		final mxGraphComponent graphComponent = eetree.print();
		getContentPane().add(graphComponent);
		// System.out.println(eetree.calculateThroughput());

	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Main frame = new Main();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1600, 1600);
		frame.setVisible(true);
	}

}
