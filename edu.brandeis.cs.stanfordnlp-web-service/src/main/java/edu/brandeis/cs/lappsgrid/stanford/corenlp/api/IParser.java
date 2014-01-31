package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;

import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * <i>IParser.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> <a href="http://nlp.stanford.edu/software/corenlp.shtml">Parser</a>
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface IParser extends WebService {
	public static final String PROP_COMPNENT_MODEL = "Parser";
	
	public static final long [] TYPES_REQUIRES = new long[] { Types.STANFORD, Types.SENTENCE  };
	public static final long [] TYPES_PRODUCES = new long[] { Types.STANFORD, Types.SENTENCE, Types.TOKEN };
	
	
	public String parse(String sentence);
}
