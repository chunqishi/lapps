package edu.brandeis.cs.python.utils;
import java.io.IOException;
import java.net.InetAddress;

import net.razorvine.pyro.NameServerProxy;
import net.razorvine.pyro.PyroProxy;

/**
 * 
 * @refer http://pythonhosted.org/Pyro4/pyrolite.html
 * @author shicq
 *
 */
public class Pyro4 {
	NameServerProxy ns = null;
	PyroProxy remoteobject = null;
	
	
	public Pyro4() throws IOException{
	    ns = NameServerProxy.locateNS("chilote.cs.brandeis.edu");
	    remoteobject = new PyroProxy(ns.lookup("PYRONAME:Holder"));
	}
	
	
	
	public static final void main(String []args) throws IOException{
		
		
		String hostname = InetAddress.getLocalHost().getHostName();
		System.out.println(hostname);
	    NameServerProxy ns = NameServerProxy.locateNS("chilote.cs.brandeis.edu");
	    PyroProxy remoteobject = new PyroProxy(ns.lookup("PYRONAME:Holder"));
	    Object result = remoteobject.call("get", "key");
	    String message = (String)result.toString();  // cast to the type that 'pythonmethod' returns
	    System.out.println(result.getClass());
	    System.out.println("result message="+message);
	    remoteobject.close();
	    ns.close();
	}
}
