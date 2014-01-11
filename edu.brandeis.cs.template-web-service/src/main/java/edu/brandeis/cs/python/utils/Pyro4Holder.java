package edu.brandeis.cs.python.utils;
import java.io.IOException;
import java.net.InetAddress;

import net.razorvine.pyro.NameServerProxy;
import net.razorvine.pyro.PyroProxy;

/**
 * Provide and interface co-work with the src/main/resource/lapps_pyro4_holder.py
 * <p>1. It allows read the Holder's function get() and put().
 * <p>2. It read the lapps.conf file to locate another lapps_pyro4_holder.py files. 
 * @refer http://pythonhosted.org/Pyro4/pyrolite.html
 * @author shicq@cs.brandeis.edu
 *
 */
public class Pyro4Holder {
	private NameServerProxy ns = null;
	private PyroProxy remoteobject = null;
	public static final String PYRONAME = "Holder";
	String hostname = null;
	
	PythonRunner pyRun = null;
	
	public Pyro4Holder() throws IOException, PythonRunnerException{
		// read local sever hostname
		hostname = InetAddress.getLocalHost().getHostName();
		pyRun = new PythonRunner(); 
	}

	public Pyro4Holder(String confPath) throws IOException, PythonRunnerException{
		// read local sever hostname
		hostname = InetAddress.getLocalHost().getHostName();
		pyRun = new PythonRunner(confPath); 
	}

	public Pyro4Holder(PythonRunner pyRun) throws IOException, PythonRunnerException{
		// read local sever hostname
		hostname = InetAddress.getLocalHost().getHostName();
		this.pyRun = pyRun; 
	}
	
	// put object into holder, using key
	public Object put(String key, Object obj) throws IOException, PythonRunnerException{
		pyRun.startPyro4Holder();
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
	    PyroProxy remoteobject = new PyroProxy(ns.lookup("Holder"));
	    Object result = remoteobject.call("put", "key", new String[]{"'asdf'"});
	    String message = (String)result.toString();  // cast to the type that 'pythonmethod' returns
	    System.out.println(result.getClass());
	    System.out.println("result message=" + message);
	    remoteobject.close();
	    ns.close();
	}
}
