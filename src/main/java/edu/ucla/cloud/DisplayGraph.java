/**
 *
 */
package edu.ucla.cloud;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;

/**
 * @author Josh
 * 
 */
class Display extends JFrame {
	public Display(final mxGraphComponent graphComponent) {
		getContentPane().add(graphComponent);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1600, 1600);

	}

	public void activate() {
		this.setVisible(true);
	}
}
