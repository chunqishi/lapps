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
public class Pyro4Holder {
	private NameServerProxy ns = null;
	private PyroProxy remoteobject = null;
	public static final String PYRONAME = "PYRONAME:Holder";
	String hostname = null;
	
	public Pyro4Holder() throws IOException{
		// read local sever hostname
		hostname = InetAddress.getLocalHost().getHostName();
	}
	
	// put object into holder, using key
	public Object put(String key, Object obj) throws IOException{
	    ns = NameServerProxy.locateNS(hostname);
	    remoteobject = new PyroProxy(ns.lookup(PYRONAME));
	    Object result = remoteobject.call("put", key, obj);
	    remoteobject.close();
	    ns.close();
		return result;
	}
	
	// read object from holder, using key
	public Object get(String key) throws IOException{
	    ns = NameServerProxy.locateNS(hostname);
	    remoteobject = new PyroProxy(ns.lookup(PYRONAME));
	    Object result = remoteobject.call("get", key);
	    remoteobject.close();
	    ns.close();
		return result;
	}

	public static final void main(String []args) throws IOException{
		String hostname = InetAddress.getLocalHost().getHostName();
		System.out.println(hostname);
	    NameServerProxy ns = NameServerProxy.locateNS(hostname);
	    PyroProxy remoteobject = new PyroProxy(ns.lookup("PYRONAME:Holder"));
	    Object result = remoteobject.call("get", "key");
	    String message = (String)result.toString();  // cast to the type that 'pythonmethod' returns
	    System.out.println(result.getClass());
	    System.out.println("result message="+message);
	    remoteobject.close();
	    ns.close();
	}
}
