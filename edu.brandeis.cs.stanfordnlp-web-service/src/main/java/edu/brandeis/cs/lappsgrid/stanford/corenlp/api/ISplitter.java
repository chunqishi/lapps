package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;

import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * <i>ISplitter.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p><a href="http://nlp.stanford.edu/software/corenlp.shtml">Sentence Detector</a> 
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface ISplitter extends WebService {
	public static final String PROP_COMPNENT_MODEL = "Sentence-Detector";
	public static final long [] TYPES_REQUIRES = new long[] { Types.STANFORD, Types.DOCUMENT, Types.TEXT };
	public static final long [] TYPES_PRODUCES = new long[] { Types.STANFORD, Types.SENTENCE, Types.STRING_LIST };
	
	public String [] split(String docs);
}
