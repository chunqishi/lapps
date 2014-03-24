package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import edu.brandeis.cs.lappsgrid.api.opennlp.IVersion;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Sequence;

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
public class POSTagger extends AbstractWebService implements IPOSTagger  {
    protected static final Logger logger = LoggerFactory.getLogger(POSTagger.class);
    
    private static opennlp.tools.postag.POSTagger postagger;
    
    
	public POSTagger() throws OpenNLPWebServiceException {
        if (postagger == null)
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

        Container container = null;
        try {
            container = getContainer(data);
        } catch (LappsException e) {
            return DataFactory.error(e.getMessage());
        }
        String[] tags = tag(new String[]{data.getPayload()});
        // steps
        ProcessingStep step = new ProcessingStep();
        // steps metadata
        step.getMetadata().put(Metadata.PRODUCED_BY, this.getClass().getName() + ":" + VERSION);
        step.getMetadata().put(Metadata.CONTAINS, "Splitter");

        //
        IDGenerator id = new IDGenerator();

        for (String tag: tags) {
            org.anc.lapps.serialization.Annotation ann =
                    new org.anc.lapps.serialization.Annotation();
            ann.setId(id.generate("tok"));
            ann.setLabel(Annotations.SENTENCE);
            Map<String, String> features = ann.getFeatures();
            putFeature(features, Features.PART_OF_SPEECH, tag);

            step.addAnnotation(ann);
        }

        if (data.getDiscriminator() != Types.TEXT)
        {
            String type = DiscriminatorRegistry.get(data.getDiscriminator());
            logger.error("execute(): Invalid input, expected TEXT, found " + type);
            return DataFactory.error("execute(): Invalid input, expected TEXT, found " + type);
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
