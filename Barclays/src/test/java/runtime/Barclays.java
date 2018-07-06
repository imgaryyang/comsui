/**
 * 
 */
package runtime;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Jetty Server starter. Use mockMode(Enable mock service implement)
 * 
 * @author 
 *
 */
public class Barclays {

	/**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        long begin = System.currentTimeMillis();

		WebAppContext webapp = new WebAppContext("webapp", "/barclays");
		webapp.setDefaultsDescriptor("./src/test/resources/runtime/webdefault.xml");
		
		// add mock web.xml here to replace the default web.xml under webapp and enable mockMode
		webapp.setDescriptor("./src/test/resources/runtime/web.local.xml");
		
		Server server = new Server(9099);
        server.setHandler(webapp);
        server.start();
        System.out.println("Jetty Server started, use " + (System.currentTimeMillis() - begin) + " ms");
    }
}
