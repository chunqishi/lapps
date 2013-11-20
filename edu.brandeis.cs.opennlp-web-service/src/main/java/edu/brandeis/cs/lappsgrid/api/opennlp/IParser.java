package edu.brandeis.cs.lappsgrid.api.opennlp;

import opennlp.tools.parser.Parser;

import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * <i>IParser.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> <a href="http://opennlp.apache.org/documentation/manual/opennlp.html#tools.parser">Parser</a>
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface IParser extends Parser, WebService {
	public static final long [] TYPES_REQUIRES = new long[] { Types.OPENNLP, Types.SENTENCE  };
	public static final long [] TYPES_PRODUCES = new long[] { Types.OPENNLP, Types.SENTENCE, Types.TOKEN };

}
