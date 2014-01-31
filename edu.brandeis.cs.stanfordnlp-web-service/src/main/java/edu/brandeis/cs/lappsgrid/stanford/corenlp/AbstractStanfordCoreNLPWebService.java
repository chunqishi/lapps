package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import java.util.Properties;

import org.anc.lapps.serialization.Container;
import org.lappsgrid.api.Data;
import org.lappsgrid.api.WebService;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * <a href="http://nlp.stanford.edu/software/corenlp.shtml" target="_blank">
 * Stanford Core NLP </a> provides a collection of available NLP tools,
 * including "tokenize, ssplit, pos, lemma, ner, parse, dcoref".
 * 
 * <p>
 * 
 * They are available through unique interface called annotation.
 * 
 * @author shicq@cs.brandeis.edu
 * 
 */
public abstract class AbstractStanfordCoreNLPWebService implements WebService {
	protected static final Logger logger = LoggerFactory
			.getLogger(AbstractStanfordCoreNLPWebService.class);

	public static final String PROP_TOKENIZE = "tokenize";
	public static final String PROP_SENTENCE_SPLIT = "ssplit";
	public static final String PROP_POS_TAG = "pos";
	public static final String PROP_LEMMA = "lemma";
	public static final String PROP_NER = "ner";
	public static final String PROP_PARSE = "parse";
	public static final String PROP_CORERENCE = "dcoref";

	protected Properties props = new Properties();
	StanfordCoreNLP snlp = null;

	public AbstractStanfordCoreNLPWebService() {
		this.init("tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	}

	protected void init(String toolList) {
		props.clear();
		props.put("annotators", toolList);
		snlp = new StanfordCoreNLP(props);
	}

	@Override
	public Data configure(Data input) {
		return DataFactory.ok();
	}

	protected Container createContainer(Data input) {
		Container container = null;
		long inputType = input.getDiscriminator();
		if (inputType == Types.ERROR) {
			return null;
		} else if (inputType == Types.TEXT) {
			container = new Container();
			container.setText(input.getPayload());
		} else if (inputType == Types.JSON) {
			container = new Container(input.getPayload());
		}
		return container;
	}

}
