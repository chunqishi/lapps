package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;

import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * <i>ITokenizer.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p><a href="http://nlp.stanford.edu/software/corenlp.shtml">Tokenizer</a> 
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface ITokenizer extends WebService  {
	public static final String PROP_COMPNENT_MODEL = "Tokenizer";
	
	public static final long [] TYPES_REQUIRES = new long[] { Types.SENTENCE  };
	public static final long [] TYPES_PRODUCES = new long[] { Types.STANFORD, Types.SENTENCE, Types.TOKEN };
	
	public String[] tokenize(String s);
}
