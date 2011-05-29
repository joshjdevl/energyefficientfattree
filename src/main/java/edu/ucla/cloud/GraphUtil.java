/**
 *
 */
package edu.ucla.cloud;

import java.util.List;

/**
 * @author Josh
 * 
 */
public class GraphUtil {

	public void removeAllServerEdges(final List<Server> serversGlobal) {
		for (final Server server : serversGlobal) {

			server.setActive(false);
		}
	}
}
