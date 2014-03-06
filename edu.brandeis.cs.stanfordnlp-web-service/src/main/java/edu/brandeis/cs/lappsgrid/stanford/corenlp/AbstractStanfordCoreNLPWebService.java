package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.anc.lapps.serialization.Container;
import org.lappsgrid.api.Data;
import org.lappsgrid.api.LappsException;
import org.lappsgrid.api.WebService;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.DiscriminatorRegistry;
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

    static protected ConcurrentHashMap<String, StanfordCoreNLP> cache =
            new ConcurrentHashMap<String, StanfordCoreNLP>();

	public static final String PROP_TOKENIZE = "tokenize";
	public static final String PROP_SENTENCE_SPLIT = "ssplit";
	public static final String PROP_POS_TAG = "pos";
	public static final String PROP_LEMMA = "lemma";
	public static final String PROP_NER = "ner";
	public static final String PROP_PARSE = "parse";
	public static final String PROP_CORERENCE = "dcoref";
    public static final String PROP_KEY = "annotators";

	protected Properties props = new Properties();
	StanfordCoreNLP snlp = null;

	public AbstractStanfordCoreNLPWebService() {
//		this.init("tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	}


    protected void init(String ... tools) {
        props.clear();

        StringBuilder sb = new StringBuilder();
        for(String tool: tools) {
            sb.append(tool).append(" ");
        }
        props.put(PROP_KEY, sb.toString().trim());
        snlp = getCached(props);
    }

	protected void init(String toolList) {
		props.clear();
		props.put(PROP_KEY, toolList);
		snlp = getCached(props);
	}


    protected StanfordCoreNLP getCached (Properties props) {
        String key = props.getProperty(PROP_KEY);
        System.out.println("-----------------");
        System.out.println(key);
        StanfordCoreNLP val = cache.get(key);
        if (val == null) {
            val = new StanfordCoreNLP(props);
            cache.put(key, val);
        }
        return val;
    }


	@Override
	public Data configure(Data input) {
		return DataFactory.ok();
	}

    public static final Container getContainer(Data input) throws LappsException
    {
        long type = input.getDiscriminator();
        if (type == Types.ERROR) {
            // Data objects with an ERROR discriminator should not be
            // passed in.
            throw new LappsException(input.getPayload());
        }
        else if (type == Types.TEXT) {
            Container container = new Container();
            container.setText(input.getPayload());
            return container;
        }
        else if (type == Types.JSON) {
            return new Container(input.getPayload());
        }
        String typeName = DiscriminatorRegistry.get(type);
        throw new LappsException("Unexpected Data object type: " + typeName);
    }
}
