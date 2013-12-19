package edu.brandeis.cs.python.examples;

import edu.brandeis.cs.python.utils.PythonRunner;
import edu.brandeis.cs.python.utils.PythonRunnerException;

public class HelloWorld implements IHelloWorld{
	private PythonRunner pr = null;
	
	public HelloWorld() throws PythonRunnerException{
		pr = new PythonRunner();
	}
	
	public HelloWorld(PythonRunner pr) throws PythonRunnerException{
		this.pr = pr;
	}

	@Override
	public String say(String world) throws PythonRunnerException {		
		String defSectionName = this.getClass().getCanonicalName();
		String ret = pr.runPythonSection(defSectionName, world);
		return ret;
	}		
	
	public String name(){
		return this.getClass().getCanonicalName();
	}
}
