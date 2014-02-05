package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Sequence;

import org.anc.resource.ResourceLoader;
import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.brandeis.cs.lappsgrid.api.opennlp.IPOSTagger;

/**
 * <i>POSTagger.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p><a href="http://opennlp.sourceforge.net/models-1.5/">Models for 1.5 series</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class POSTagger implements IPOSTagger {
    protected static final Logger logger = LoggerFactory.getLogger(POSTagger.class);
    
    private opennlp.tools.postag.POSTagger postagger;
    
    
	public POSTagger() throws OpenNLPWebServiceException {
		init();
	}
    
	protected void init() throws OpenNLPWebServiceException {
	      logger.info("init(): Creating OpenNLP POSTagger ...");
	      
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
	      String taggerModel = prop.getProperty(PROP_COMPNENT_MODEL__Maxent, "en-pos-maxent.bin");
	      
	      logger.info("init(): load opennlp-web-service.properties.");
	      
	      stream = ResourceLoader.open(taggerModel);
	      if (stream == null) {
	    	  logger.error("init(): fail to open POSTAGGER MODEl \""+taggerModel+"\".");
	    	  throw new OpenNLPWebServiceException("init(): fail to open POSTAGGER MODEl \""+taggerModel+"\".");
	      }
	      
	      logger.info("init(): load POSTAGGER MODEl \""+taggerModel+"\"");
	     
		try {
			try {
				POSModel model = new POSModel(stream);
				postagger = new POSTaggerME(model);
			} finally {
				stream.close();
			}
		} catch (IOException e) {
	    	  logger.error("init(): fail to load POSTAGGER MODEl \""+taggerModel+"\".");
	    	  throw new OpenNLPWebServiceException("init(): fail to load POSTAGGER MODEl \""+taggerModel+"\".");
		}
		
      logger.info("init(): Creating OpenNLP POSTagger!");
	}

	@Override
	public Data configure(Data data) {
		return DataFactory.ok();
	}

	@Override
	public Data execute(Data data) {
		logger.info("execute(): Execute OpenNLP POSTagger ...");

		if (postagger == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				logger.error("execute(): Fail to initialize POSTagger");
				return DataFactory
						.error("execute(): Fail to initialize POSTagger");
			}
		}
		String[] sentences = data.getPayload().split("\\n+");
		String[] tags = tag(sentences);
		logger.info("execute(): Execute OpenNLP POSTagger!");
		return DataFactory.stringList(tags);
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
	public String[] tag(String[] sentence) {
		if (postagger == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				throw new RuntimeException("tokenize(): Fail to initialize POSTagger", e);
			}
		}
		String tags[] = postagger.tag(sentence);
		return tags;
	}


	@Override
	public Sequence[] topKSequences(String[] sentence) {
		if (postagger == null) {
			try {
				init();
			} catch (OpenNLPWebServiceException e) {
				throw new RuntimeException("tokenize(): Fail to initialize POSTagger", e);
			}
		}
		Sequence tags[] = postagger.topKSequences(sentence);
		return tags;
	}

}
