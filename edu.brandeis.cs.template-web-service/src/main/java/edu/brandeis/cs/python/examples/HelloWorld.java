package edu.brandeis.cs.python.examples;

import edu.brandeis.cs.python.utils.PythonRunner;
import edu.brandeis.cs.python.utils.PythonRunnerException;

public class HelloWorld implements IHelloWorld{
	
	
	private PythonRunner pr = null;
	
	public HelloWorld() throws PythonRunnerException{
		pr = new PythonRunner();
	}

	@Override
	public String say(String world) throws PythonRunnerException {		
		String ret = pr.runPythonSection("helloworld.say", world);
		return ret;
	}
	
	public static final void main(String []args) throws PythonRunnerException{
		HelloWorld hw = new HelloWorld();
		System.out.println("say=" + hw.say("world"));
		PythonRunner pr = new PythonRunner();
		String s = pr.runPythonSection("helloworld.append", "hello", "world");
	}

}
