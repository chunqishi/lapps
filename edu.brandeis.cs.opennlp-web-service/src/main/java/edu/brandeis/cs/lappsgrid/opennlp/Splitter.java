package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;

import org.anc.resource.ResourceLoader;
import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.DiscriminatorRegistry;
import org.lappsgrid.discriminator.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.brandeis.cs.lappsgrid.api.opennlp.ISplitter;

/**
 * <i>AbstractOpenNLPWebService.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p><a href="http://opennlp.sourceforge.net/models-1.5/">Models for 1.5 series</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class Splitter implements ISplitter {
    protected static final Logger logger = LoggerFactory.getLogger(Splitter.class);
    
    private SentenceDetector sentenceDetector;
    
    
	public Splitter() throws OpenNLPWebServiceException {
		init();
	}
    
	protected void init() throws OpenNLPWebServiceException {
	      logger.info("init(): Creating OpenNLP SentenceDetector ...");
	      
	      Properties prop = new Properties();          
	      InputStream stream = ResourceLoader.open("opennlp-web-service.properties");
	      if (stream == null) {
	    	  logger.error("init(): fail to open \"opennlp-web-service.properties\".");
	    	  throw new OpenNLPWebServiceException("init(): fail to open \"opennlp-web-service.properties\".");
	      }
	      try {
	    	  prop.load(stream);
	    	  stream.close();
	      } catch (IOException e) {
	    	  logger.error("init(): fail to load \"opennlp-web-service.properties\".");
	    	  throw new OpenNLPWebServiceException("init(): fail to load \"opennlp-web-service.properties\".");
	      }
	      
	      // default English
	      String sentenceModel = prop.getProperty(PROP_COMPNENT_MODEL, "en-sent.bin");
	      
	      logger.info("init(): load opennlp-web-service.properties.");
	      
	      stream = ResourceLoader.open(sentenceModel);
	      if (stream == null) {
	    	  logger.error("init(): fail to open SENTENCE MODEl \""+sentenceModel+"\".");
	    	  throw new OpenNLPWebServiceException("init(): fail to open TOKEN MODEl \""+sentenceModel+"\".");
	      }
	      
	      logger.info("init(): load TOKEN MODEl \""+sentenceModel+"\"");
	     
		try {
			try {
				SentenceModel model = new SentenceModel(stream);
				sentenceDetector = new SentenceDetectorME(model);
			} finally {
				stream.close();
			}
		} catch (IOException e) {
	    	  logger.error("init(): fail to load SENTENCE MODEl \""+sentenceModel+"\".");
	    	  throw new OpenNLPWebServiceException("init(): fail to load SENTENCE MODEl \""+sentenceModel+"\".");
		}
		
      logger.info("init(): Creating OpenNLP SentenceDetector!");
	}

	@Override
	public Data configure(Data data) {
		return DataFactory.ok();
	}

	@Override
	public Data execute(Data data) {
		logger.info("execute(): Execute OpenNLP SentenceDetector ...");

		if (sentenceDetector == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				logger.error("execute(): Fail to initialize SentenceDetector");
				return DataFactory
						.error("execute(): Fail to initialize SentenceDetector");
			}
		}
		
	    if (data.getDiscriminator() != Types.TEXT)
	    {
	         String type = DiscriminatorRegistry.get(data.getDiscriminator());
	         logger.error("execute(): Invalid input, expected TEXT, found " + type);
	         return DataFactory.error("execute(): Invalid input, expected TEXT, found " + type);
	    }

		String[] sentences = sentDetect(data.getPayload());
		logger.info("execute(): Execute OpenNLP SentenceDetector!");
		return DataFactory.stringList(sentences);
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
	public String[] sentDetect(String s) {
		if (sentenceDetector == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				throw new RuntimeException("sentDetect(): Fail to initialize SentenceDetector", e);
			}
		}
		
		String sentences[] = sentenceDetector.sentDetect(s);
		return sentences;
	}

	@Override
	public Span[] sentPosDetect(String s) {
		if (sentenceDetector == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				throw new RuntimeException("sentPosDetect(): Fail to initialize SentenceDetector", e);
			}
		}
		Span [] offsets = sentenceDetector.sentPosDetect(s);
		return offsets;
	}

}
