package edu.brandeis.cs.lappsgrid.api.opennlp;

import opennlp.tools.util.Span;

import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * <i>INamedEntityRecognizer.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> <a href="http://opennlp.apache.org/documentation/manual/opennlp.html#tools.namefind.recognition">Named Entity Recognition</a>
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface INamedEntityRecognizer extends WebService {

	public static final String PROP_COMPNENT_MODEL = "Name-Finder";
	public static final String TOKEN_SPAN_SPLIT = ":";
	
	public static final long [] TYPES_REQUIRES = new long[] { Types.OPENNLP, Types.SENTENCE  };
	public static final long [] TYPES_PRODUCES = new long[] { Types.OPENNLP, Types.SENTENCE, Types.TOKEN };
	
	/**
	 *  Generates name tags for the given sequence, typically a sentence, returning token spans for any identified names.
	 * 
	 * @see opennlp.tools.namefind.TokenNameFinder
	 */
	public Span[] find(String[] tokens) ;
}
