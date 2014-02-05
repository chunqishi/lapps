package edu.brandeis.cs.lappsgrid.opennlp;

/**
 * <i>OpenNLPWebServiceException.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class OpenNLPWebServiceException extends Exception {
	/**
	 * long 
	 */
	private static final long serialVersionUID = -1150592185232159968L;

	
	public OpenNLPWebServiceException() {
		super("OpenNLP-Web-Service-Exception:");
	}
	
	public OpenNLPWebServiceException(String message) {
		super("OpenNLP-Web-Service-Exception:" + message);
	}
	
	public OpenNLPWebServiceException(String message, Throwable cause) {
        super("OpenNLP-Web-Service-Exception:" + message, cause);
    }
	
}
