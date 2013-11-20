package edu.brandeis.cs.lappsgrid.api.opennlp;

import opennlp.tools.postag.POSTagger;

import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * <i>IPOSTagger.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p><a href="http://opennlp.apache.org/documentation/manual/opennlp.html#tools.postagger">Part-of-Speech Tagger</a> 
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface IPOSTagger extends POSTagger, WebService {
	public static final String PROP_COMPNENT_MODEL__Maxent = "Part-of-Speech-Tagger";
	
	public static final String PROP_COMPNENT_MODEL__Perceptron = "POS-Tagger";
	
	public static final long [] TYPES_REQUIRES = new long[] { Types.OPENNLP, Types.SENTENCE  };
	public static final long [] TYPES_PRODUCES = new long[] { Types.OPENNLP, Types.SENTENCE, Types.TOKEN };

}
