package edu.brandeis.cs.lappsgrid.api.opennlp;

import opennlp.tools.sentdetect.SentenceDetector;

import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * <i>ISplitter.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p><a href="http://opennlp.apache.org/documentation/manual/opennlp.html#tools.sentdetect.detection.api">Sentence Detector</a> 
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface ISplitter extends SentenceDetector, WebService {
	public static final String PROP_COMPNENT_MODEL = "Sentence-Detector";
	public static final long [] TYPES_REQUIRES = new long[] { Types.OPENNLP, Types.TEXT };
	public static final long [] TYPES_PRODUCES = new long[] { Types.OPENNLP, Types.SENTENCE };
}
