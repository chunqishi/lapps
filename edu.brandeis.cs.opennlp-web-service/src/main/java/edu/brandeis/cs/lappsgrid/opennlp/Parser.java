package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import org.anc.resource.ResourceLoader;
import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
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
public class Parser implements IParser {
	protected static final Logger logger = LoggerFactory
			.getLogger(Parser.class);

	private opennlp.tools.parser.Parser parser;

	public Parser() throws OpenNLPWebServiceException {
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

		if (parser == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				logger.error("execute(): Fail to initialize Parser");
				return DataFactory
						.error("execute(): Fail to initialize Parser");
			}
		}

		String parser = parse(data.getPayload());
		logger.info("execute(): Execute OpenNLP Parser!");
		return DataFactory.text(parser);
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
