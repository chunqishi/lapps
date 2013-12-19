package edu.brandeis.cs.python.utils;


/**
 * @brief Exception handler for Python Runner 
 * @author shicq@cs.brandeis.edu
 * 
 *
 */
public class PythonRunnerException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5519418141743137711L;
	
	public PythonRunnerException(){
		super();
	}
	
	public PythonRunnerException(String msg){
		super(msg);
	}
	
	public PythonRunnerException(String msg, Throwable th){
		super(msg, th);
	}
	
	public PythonRunnerException(Throwable th) {
		super(th);
	}

}
