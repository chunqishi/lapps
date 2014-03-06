package edu.brandeis.cs.lappsgrid.api.opennlp;

import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * <i>ITokenizer.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p><a href="http://opennlp.apache.org/documentation/manual/opennlp.html#tools.tokenizer">Tokenizer</a> 
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface ITokenizer extends opennlp.tools.tokenize.Tokenizer, WebService  {
	public static final String PROP_COMPNENT_MODEL = "Tokenizer";
	
	public static final long [] TYPES_REQUIRES = new long[] { Types.SENTENCE  };
	public static final long [] TYPES_PRODUCES = new long[] { Types.OPENNLP, Types.SENTENCE, Types.TOKEN };

}
