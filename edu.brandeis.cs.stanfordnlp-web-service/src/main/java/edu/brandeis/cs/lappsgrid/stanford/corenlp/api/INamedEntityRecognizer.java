package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;


import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * <i>INamedEntityRecognizer.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> <a href="http://nlp.stanford.edu/software/corenlp.shtml">Named Entity Recognition</a>
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface INamedEntityRecognizer extends WebService {
	public static final String PROP_COMPNENT_MODEL = "Name-Finder";
	public static final long [] TYPES_REQUIRES = new long[] { Types.POS };
	public static final long [] TYPES_PRODUCES = new long[] { Types.STANFORD, Types.NAMED_ENTITES };
	
	/**
	 *  Generates name tags for the given sequence, typically a sentence, returning token spans for any identified names.
	 * 
	 * @see{opennlp.tools.namefind.TokenNameFinder}
	 */
	public String find(String docs) ;
}
