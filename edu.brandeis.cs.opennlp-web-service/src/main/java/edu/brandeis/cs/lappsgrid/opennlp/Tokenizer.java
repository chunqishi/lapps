package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import org.anc.resource.ResourceLoader;
import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.brandeis.cs.lappsgrid.api.opennlp.ITokenizer;

/**
 * <i>AbstractOpenNLPWebService.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p><a href="http://opennlp.sourceforge.net/models-1.5/">Models for 1.5 series</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class Tokenizer implements ITokenizer {
    protected static final Logger logger = LoggerFactory.getLogger(Tokenizer.class);
    
    private opennlp.tools.tokenize.Tokenizer tokenizer;
    
    
	public Tokenizer() throws OpenNLPWebServiceException {
		init();
	}
    
	protected void init() throws OpenNLPWebServiceException {
	      logger.info("init(): Creating OpenNLP Tokenizer ...");
	      
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
	      String tokenModel = prop.getProperty(PROP_COMPNENT_MODEL, "en-token.bin");
	      
	      logger.info("init(): load opennlp-web-service.properties.");
	      
	      stream = ResourceLoader.open(tokenModel);
	      if (stream == null) {
	    	  logger.error("init(): fail to open TOKEN MODEl \""+tokenModel+"\".");
	    	  throw new OpenNLPWebServiceException("init(): fail to open TOKEN MODEl \""+tokenModel+"\".");
	      }
	      
	      logger.info("init(): load TOKEN MODEl \""+tokenModel+"\"");
	     
		try {
			try {
				TokenizerModel model = new TokenizerModel(stream);
				tokenizer = new TokenizerME(model);
			} finally {
				stream.close();
			}
		} catch (IOException e) {
	    	  logger.error("init(): fail to load TOKEN MODEl \""+tokenModel+"\".");
	    	  throw new OpenNLPWebServiceException("init(): fail to load TOKEN MODEl \""+tokenModel+"\".");
		}
		
      logger.info("init(): Creating OpenNLP Tokenizer!");
	}

	@Override
	public Data configure(Data data) {
		return DataFactory.ok();
	}

	@Override
	public Data execute(Data data) {
		logger.info("execute(): Execute OpenNLP tokenizer ...");

		if (tokenizer == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				logger.error("execute(): Fail to initialize Tokenizer");
				return DataFactory
						.error("execute(): Fail to initialize Tokenizer");
			}
		}

		String[] tokens = tokenize(data.getPayload());
		logger.info("execute(): Execute OpenNLP tokenizer!");
		return DataFactory.stringList(tokens);
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
	public String[] tokenize(String s) {
		if (tokenizer == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				throw new RuntimeException("tokenize(): Fail to initialize Tokenizer", e);
			}
		}
		String tokens[] = tokenizer.tokenize(s);
		return tokens;
	}

	@Override
	public Span[] tokenizePos(String s) {
		if (tokenizer == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				throw new RuntimeException("tokenize(): Fail to initialize Tokenizer", e);
			}
		}
		Span [] boundaries = tokenizer.tokenizePos(s);
		return boundaries;
	}

}
