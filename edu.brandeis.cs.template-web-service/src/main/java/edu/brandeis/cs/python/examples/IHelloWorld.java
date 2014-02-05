package edu.brandeis.cs.python.examples;

import jp.go.nict.langrid.commons.rpc.intf.Service;
import edu.brandeis.cs.python.utils.PythonRunnerException;

/**
 * @brief  Simple example to create a Python service.
 * @file examples/helloworld.py
 * 
 * @author shicq@cs.brandeis.edu
 *
 */


@Service(namespace="servicegrid:servicetype:brandeis:HelloWorld")
public interface IHelloWorld {
	public String say(String world) throws PythonRunnerException;
}
