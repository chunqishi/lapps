package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import edu.brandeis.cs.lappsgrid.api.opennlp.IVersion;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.util.Span;

import org.anc.lapps.serialization.Container;
import org.anc.lapps.serialization.ProcessingStep;
import org.anc.resource.ResourceLoader;
import org.anc.util.IDGenerator;
import org.lappsgrid.api.Data;
import org.lappsgrid.api.LappsException;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.vocabulary.Annotations;
import org.lappsgrid.vocabulary.Features;
import org.lappsgrid.vocabulary.Metadata;
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
public class NamedEntityRecognizer extends AbstractWebService implements INamedEntityRecognizer  {
	protected static final Logger logger = LoggerFactory
			.getLogger(NamedEntityRecognizer.class);

	private static ArrayList<TokenNameFinder> nameFinders = new ArrayList<TokenNameFinder> ();

	public NamedEntityRecognizer() throws OpenNLPWebServiceException {
        if (nameFinders.size() == 0)
		    init();
	}

	protected static final TokenNameFinder load(String nerModel) throws OpenNLPWebServiceException {
		TokenNameFinder nameFinder;
		InputStream stream = ResourceLoader.open(nerModel);
		if (stream == null) {
			logger.error("load(): fail to open NER MODEL \"" + nerModel
					+ "\".");
			throw new OpenNLPWebServiceException(
					"load(): fail to open NER MODEL \"" + nerModel + "\".");
		}

		logger.info("load(): load NER MODEL \"" + nerModel + "\"");

		try {
			try {
				TokenNameFinderModel model = new TokenNameFinderModel(stream);
				nameFinder = new NameFinderME(model);
			} finally {
				stream.close();
			}
		} catch (IOException e) {
			logger.error("load(): fail to load NER MODEL \"" + nerModel
					+ "\".");
			throw new OpenNLPWebServiceException(
					"load(): fail to load NER MODEL \"" + nerModel + "\".");
		}
		return nameFinder;
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
		String nerModels = prop.getProperty(PROP_COMPNENT_MODEL,
				"en-ner-person.bin");
		logger.info("init(): load opennlp-web-service.properties.");

		for (String nerModel : nerModels.split(":")) {
			logger.info("init(): load " + nerModel + " ...");
			if (nerModel.trim().length() > 0) {
				TokenNameFinder nameFinder = load(nerModel);
				if (nameFinder != null)
					nameFinders.add(nameFinder);
			}
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


        Container container = null;
        try {
            container = getContainer(data);
        } catch (LappsException e) {
            return DataFactory.error(e.getMessage());
        }

        // steps
        ProcessingStep step = new ProcessingStep();
        // steps metadata
        step.getMetadata().put(Metadata.PRODUCED_BY, this.getClass().getName() + ":" + VERSION);
        step.getMetadata().put(Metadata.CONTAINS, "ner");

        //
        IDGenerator id = new IDGenerator();

        String[] tokens = data.getPayload().split("\\n+");
        // spans for each of the names identified
        Span[] spans = find(tokens);
        String[] strSpans = new String[spans.length];
        for (int i = 0; i < spans.length; i++) {
            org.anc.lapps.serialization.Annotation ann =
                    new org.anc.lapps.serialization.Annotation();
            ann.setId(id.generate("tok"));
            ann.setLabel(Annotations.TOKEN);
            ann.setStart(spans[i].getStart());
            ann.setEnd(spans[i].getEnd());
            Map<String, String> features = ann.getFeatures();
            putFeature(features, Features.NER, spans[i].getType());
            step.addAnnotation(ann);
        }

        container.getSteps().add(step);
        logger.info("execute(): Execute OpenNLP NamedEntityRecognizer!");
        return DataFactory.json(container.toJson());
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
		if (nameFinders.size() == 0) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				throw new RuntimeException(
						"tokenize(): Fail to initialize NamedEntityRecognizer",
						e);
			}
		}
		ArrayList<Span> spanArr = new ArrayList<Span>(16);
		for (TokenNameFinder nameFinder : nameFinders) {
			Span[] partSpans = nameFinder.find(tokens);
			for (Span span:partSpans)
				spanArr.add(span);
		}
		
		return spanArr.toArray(new Span[spanArr.size()]);
	}

}
