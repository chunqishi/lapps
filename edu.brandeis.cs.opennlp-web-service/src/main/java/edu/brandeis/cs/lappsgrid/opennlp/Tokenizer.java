package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import edu.brandeis.cs.lappsgrid.api.opennlp.IVersion;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

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

import edu.brandeis.cs.lappsgrid.api.opennlp.ITokenizer;

/**
 * <i>Tokenizer.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p><a href="http://opennlp.sourceforge.net/models-1.5/">Models for 1.5 series</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class Tokenizer extends AbstractWebService implements ITokenizer {
    protected static final Logger logger = LoggerFactory.getLogger(Tokenizer.class);
    
    private static opennlp.tools.tokenize.Tokenizer tokenizer;
    
    
	public Tokenizer() throws OpenNLPWebServiceException {
        if (tokenizer == null)
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

        Container container = null;
        try {
            container = getContainer(data);
        } catch (LappsException e) {
            return DataFactory.error(e.getMessage());
        }

        String[] tokens = tokenize(data.getPayload());

        // steps
        ProcessingStep step = new ProcessingStep();
        // steps metadata
        step.getMetadata().put(Metadata.PRODUCED_BY, this.getClass().getName() + ":" + VERSION);
        step.getMetadata().put(Metadata.CONTAINS, Features.PART_OF_SPEECH);

        //
        IDGenerator id = new IDGenerator();

        for (String token: tokens) {
            org.anc.lapps.serialization.Annotation ann =
                    new org.anc.lapps.serialization.Annotation();
            ann.setId(id.generate("tok"));
            ann.setLabel(Annotations.TOKEN);
            Map<String, String> features = ann.getFeatures();
            putFeature(features, Features.WORD, token);

            step.addAnnotation(ann);
        }

		logger.info("execute(): Execute OpenNLP tokenizer!");
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
	public String[] tokenize(String s) {
		String tokens[] = tokenizer.tokenize(s);
		return tokens;
	}

	@Override
	public Span[] tokenizePos(String s) {
		Span [] boundaries = tokenizer.tokenizePos(s);
		return boundaries;
	}

}
