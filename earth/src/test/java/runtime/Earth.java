/**
 * 
 */
package runtime; 

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Jetty Server starter. Use mockMode(Enable mock service implement)
 * 
 * @author lute
 *
 */
public class Earth {
	
	/**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

    	String currentPath = Earth.class.getResource("").getPath();

    	System.out.print("current path:"+currentPath);

    	String webappPath = "webapp";

    	String descriptorPath = "./src/test/resources/runtime/webdefault.xml";

    	String definedDescriptorPath="./src/test/resources/runtime/web.local.xml";
    	
    	boolean isEclipse = true;

    	//检查是否是IDEA方式启动
    	if(currentPath.indexOf("target") > -1 && !isEclipse){

    		String basePath = currentPath.substring(0,currentPath.lastIndexOf("target"));

			webappPath=basePath+"/webapp";

			descriptorPath=basePath+"/src/test/resources/runtime_idea/webdefault.xml";

			definedDescriptorPath=basePath+"/src/test/resources/runtime_idea/web.local.xml";
		}
    	
        long begin = System.currentTimeMillis();

		WebAppContext webapp = new WebAppContext(webappPath, "");
		webapp.setDefaultsDescriptor(descriptorPath);
		
		// add mock web.xml here to replace the default web.xml under webapp and enable mockMode
		webapp.setDescriptor(definedDescriptorPath);
		webapp.setMaxFormContentSize(-1);
		
		Server server = new Server(9090);
		server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", "-1");
        server.setHandler(webapp);
        server.start();
        System.out.println("Jetty Server started, use " + (System.currentTimeMillis() - begin) + " ms");
    }
}
