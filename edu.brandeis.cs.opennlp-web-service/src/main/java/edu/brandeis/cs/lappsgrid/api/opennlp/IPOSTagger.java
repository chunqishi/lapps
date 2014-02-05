package edu.brandeis.cs.lappsgrid.api.opennlp;

import opennlp.tools.util.Sequence;

import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * <i>IPOSTagger.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p><a href="http://opennlp.apache.org/documentation/manual/opennlp.html#tools.postagger">Part-of-Speech Tagger</a> 
 * <p>Only two methods{tag(String[] sentence), topKSequences(String[] sentence)} from {@link opennlp.tools.postag.POSTagger} are listed.
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface IPOSTagger extends WebService {
	public static final String PROP_COMPNENT_MODEL__Maxent = "Part-of-Speech-Tagger";
	public static final String PROP_COMPNENT_MODEL__Perceptron = "POS-Tagger";
	
	public static final long [] TYPES_REQUIRES = new long[] { Types.OPENNLP, Types.SENTENCE, Types.TOKEN  };
	public static final long [] TYPES_PRODUCES = new long[] { Types.OPENNLP, Types.SENTENCE, Types.TOKEN, Types.POS  };

	  /**
	   * Assigns the sentence of tokens pos tags.
	   * @param sentence The sentece of tokens to be tagged.
	   * @return an array of pos tags for each token provided in sentence.
	   */
	  public String[] tag(String[] sentence);
	  
	  public Sequence[] topKSequences(String[] sentence);

}
