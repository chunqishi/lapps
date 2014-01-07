package edu.brandeis.cs.python.examples.nltk;


/**
 * @brief Exception handler for IClassify Web Service 
 * @author shicq@cs.brandeis.edu
 * @see IClassify
 *
 */
public class ClassifyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5519418141743137711L;
	
	public ClassifyException(){
		super();
	}
	
	public ClassifyException(String msg){
		super(msg);
	}
	
	public ClassifyException(String msg, Throwable th){
		super(msg, th);
	}
	
	public ClassifyException(Throwable th) {
		super(th);
	}

}
