package com.mxgraph.examples.swing;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class HelloWorld extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = -2707712944901661771L;

	public HelloWorld() {
		super("Hello, World!");

		final mxGraph graph = new mxGraph();
		final Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			final Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20,
					80, 30);
			final Object v2 = graph.insertVertex(parent, null, "World!", 20,
					20, 80, 30);
			graph.insertEdge(parent, null, "Edge", v1, v2);
		} finally {
			graph.getModel().endUpdate();
		}

		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}

	public static void main(final String[] args) {
		final HelloWorld frame = new HelloWorld();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setVisible(true);
	}

}
