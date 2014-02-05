/**
 * 
 */
package edu.brandeis.cs.lappsgrid.stanford;

/**
 * Define the Exception for Web Service Wrapping for Stanford NLP Tools
 * 
 * @author shicq@cs.brandeis.edu
 *
 */
public class StanfordWebServiceException extends Exception {
	/**
	 * long 
	 */
	private static final long serialVersionUID = -1150592185232159968L;

	
	public StanfordWebServiceException() {
		super("Stanford-Web-Service-Exception:");
	}
	
	public StanfordWebServiceException(String message) {
		super("Stanford-Web-Service-Exception:" + message);
	}
	
	public StanfordWebServiceException(String message, Throwable cause) {
        super("Stanford-Web-Service-Exception:" + message, cause);
    }
	
}
