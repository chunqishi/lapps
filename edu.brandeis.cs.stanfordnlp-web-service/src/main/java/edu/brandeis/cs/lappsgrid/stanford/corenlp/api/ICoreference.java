package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;


import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.Types;

/**
 * The Language Application Grid: A Framework for Rapid Adaptation and Reuse
 * <p>
 * Lapps Grid project wrapps Stanford core nlp tools here.
 * <p>
 * @author Chunqi SHI (shicq@cs.brandeis.edu) <br> Jan 28, 2014 </br>
 *
 */
public interface ICoreference extends WebService{
	public static final String PROP_COMPNENT_MODEL = "Name-Finder";
	public static final long [] TYPES_REQUIRES = new long[] { Types.STANFORD, Types.SENTENCE  };
	public static final long [] TYPES_PRODUCES = new long[] { Types.STANFORD, Types.SENTENCE, Types.TOKEN };
}
