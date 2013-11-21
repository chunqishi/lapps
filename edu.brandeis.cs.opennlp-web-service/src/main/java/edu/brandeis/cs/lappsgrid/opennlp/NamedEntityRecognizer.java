package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import org.anc.resource.ResourceLoader;
import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.brandeis.cs.lappsgrid.api.opennlp.INamedEntityRecognizer;

/**
 * <i>NamedEntityRecognizer.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p>
 * <p>
 * <a href="http://opennlp.sourceforge.net/models-1.5/">Models for 1.5
 * series</a>
 * <p>
 * 
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>
 *         Nov 20, 2013<br>
 * 
 */
public class NamedEntityRecognizer implements INamedEntityRecognizer {
	protected static final Logger logger = LoggerFactory
			.getLogger(NamedEntityRecognizer.class);

	private TokenNameFinder nameFinder;

	public NamedEntityRecognizer() throws OpenNLPWebServiceException {
		init();
	}

	protected void init() throws OpenNLPWebServiceException {
		logger.info("init(): Creating OpenNLP NamedEntityRecognizer ...");

		Properties prop = new Properties();
		InputStream stream = ResourceLoader
				.open("opennlp-web-service.properties");
		if (stream == null) {
			logger.error("init(): fail to open \"opennlp-web-service.properties\".");
			throw new OpenNLPWebServiceException(
					"init(): fail to open \"opennlp-web-service.properties\".");
		}
		try {
			prop.load(stream);
			stream.close();
		} catch (IOException e) {
			logger.error("init(): fail to load \"opennlp-web-service.properties\".");
			throw new OpenNLPWebServiceException(
					"init(): fail to load \"opennlp-web-service.properties\".");
		}

		// default English
		String nerModel = prop.getProperty(PROP_COMPNENT_MODEL,
				"en-ner-person.bin");

		logger.info("init(): load opennlp-web-service.properties.");

		stream = ResourceLoader.open(nerModel);
		if (stream == null) {
			logger.error("init(): fail to open NER MODEl \"" + nerModel
					+ "\".");
			throw new OpenNLPWebServiceException(
					"init(): fail to open NER MODEl \"" + nerModel + "\".");
		}

		logger.info("init(): load NER MODEl \"" + nerModel + "\"");

		try {
			try {
				TokenNameFinderModel model = new TokenNameFinderModel(stream);
				nameFinder = new NameFinderME(model);
			} finally {
				stream.close();
			}
		} catch (IOException e) {
			logger.error("init(): fail to load NER MODEl \"" + nerModel
					+ "\".");
			throw new OpenNLPWebServiceException(
					"init(): fail to load NER MODEl \"" + nerModel + "\".");
		}

		logger.info("init(): Creating OpenNLP NamedEntityRecognizer!");
	}

	@Override
	public Data configure(Data data) {
		return DataFactory.ok();
	}

	@Override
	public Data execute(Data data) {
		logger.info("execute(): Execute OpenNLP NamedEntityRecognizer ...");

		if (nameFinder == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				logger.error("execute(): Fail to initialize NamedEntityRecognizer");
				return DataFactory
						.error("execute(): Fail to initialize NamedEntityRecognizer");
			}
		}
		String[] tokens = data.getPayload().split("\\n+");
		// spans for each of the names identified
		Span[] spans = find(tokens);
		String[] strSpans = new String[spans.length];
		for (int i = 0; i < spans.length; i++) {
			strSpans[i] = fromSpanToString(spans[i]);
		}
		logger.info("execute(): Execute OpenNLP NamedEntityRecognizer!");
		return DataFactory.stringList(strSpans);
	}

	public static final Span fromStringToSpan(String s) {
		String[] strArr = s.split(TOKEN_SPAN_SPLIT);
		return new Span(Integer.parseInt(strArr[0]),
				Integer.parseInt(strArr[1]), strArr[2]);
	}

	public static final String fromSpanToString(Span span) {
		return new String(span.getStart() + TOKEN_SPAN_SPLIT + span.getEnd()
				+ TOKEN_SPAN_SPLIT + span.getType());
	}

	@Override
	public long[] requires() {
		return TYPES_REQUIRES;
	}

	@Override
	public long[] produces() {
		return TYPES_PRODUCES;
	}

	@Override
	public Span[] find(String[] tokens) {
		if (nameFinder == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				throw new RuntimeException(
						"tokenize(): Fail to initialize NamedEntityRecognizer",
						e);
			}
		}
		Span[] boundaries = nameFinder.find(tokens);
		return boundaries;
	}

}
