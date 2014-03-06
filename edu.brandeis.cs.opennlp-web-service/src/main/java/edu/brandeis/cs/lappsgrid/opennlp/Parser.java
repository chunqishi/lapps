package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import edu.brandeis.cs.lappsgrid.api.opennlp.IVersion;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import org.anc.lapps.serialization.Container;
import org.anc.lapps.serialization.ProcessingStep;
import org.anc.resource.ResourceLoader;
import org.anc.util.IDGenerator;
import org.lappsgrid.api.Data;
import org.lappsgrid.api.LappsException;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.DiscriminatorRegistry;
import org.lappsgrid.discriminator.Types;
import org.lappsgrid.vocabulary.Annotations;
import org.lappsgrid.vocabulary.Features;
import org.lappsgrid.vocabulary.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.brandeis.cs.lappsgrid.api.opennlp.IParser;

/**
 * <i>Parser.java</i> Language Application Grids
 * (<b>LAPPS</b>)
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
public class Parser extends AbstractWebService implements IParser {
	protected static final Logger logger = LoggerFactory
			.getLogger(Parser.class);

	private static opennlp.tools.parser.Parser parser;

	public Parser() throws OpenNLPWebServiceException {
        if (parser == null)
		    init();
	}

	protected void init() throws OpenNLPWebServiceException {
		logger.info("init(): Creating OpenNLP Parser ...");

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
		String parserModel = prop.getProperty(PROP_COMPNENT_MODEL,
				"en-parser-chunking.bin");

		logger.info("init(): load opennlp-web-service.properties.");

		stream = ResourceLoader.open(parserModel);
		if (stream == null) {
			logger.error("init(): fail to open PARSER MODEl \"" + parserModel
					+ "\".");
			throw new OpenNLPWebServiceException(
					"init(): fail to open PARSER MODEl \"" + parserModel + "\".");
		}

		logger.info("init(): load PARSER MODEl \"" + parserModel + "\"");

		try {
			try {
				ParserModel model = new ParserModel(stream);
				parser = ParserFactory.create(model);
			} finally {
				stream.close();
			}
		} catch (IOException e) {
			logger.error("init(): fail to load PARSER MODEl \"" + parserModel
					+ "\".");
			throw new OpenNLPWebServiceException(
					"init(): fail to load PARSER MODEl \"" + parserModel + "\".");
		}

		logger.info("init(): Creating OpenNLP Parser!");
	}

	@Override
	public Data configure(Data data) {
		return DataFactory.ok();
	}

	@Override
	public Data execute(Data data) {
		logger.info("execute(): Execute OpenNLP Parser ...");

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
        step.getMetadata().put(Metadata.CONTAINS, "Splitter");

        //
        IDGenerator id = new IDGenerator();

        Parse parses[] = ParserTool.parseLine(data.getPayload(), parser, 1);
        StringBuffer builder = new StringBuffer();
        for (int pi = 0, pn = parses.length; pi < pn; pi++) {
            builder.setLength(0);
            parses[pi].show(builder);
            org.anc.lapps.serialization.Annotation ann =
                    new org.anc.lapps.serialization.Annotation();
            ann.setId(id.generate("tok"));
            ann.setLabel(Annotations.TOKEN);
            Map<String, String> features = ann.getFeatures();
            putFeature(features, Features.PART_OF_SPEECH, builder.toString());
            step.addAnnotation(ann);
        }

        container.getSteps().add(step);
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
	public String parse(String sentence) {
		if (parser == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				throw new RuntimeException(
						"parse(): Fail to initialize Parser", e);
			}
		}

		StringBuffer builder = new StringBuffer();
		Parse parses[] = ParserTool.parseLine(sentence, parser, 1);

		for (int pi = 0, pn = parses.length; pi < pn; pi++) {
			parses[pi].show(builder);
			builder.append("\n");
		}

		return builder.toString();
	}

}
