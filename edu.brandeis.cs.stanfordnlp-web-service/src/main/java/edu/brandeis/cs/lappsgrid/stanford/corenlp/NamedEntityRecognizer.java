package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import org.anc.lapps.serialization.Container;
import org.anc.lapps.serialization.ProcessingStep;
import org.anc.util.IDGenerator;
import org.lappsgrid.api.Data;
import org.lappsgrid.api.LappsException;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;

import edu.brandeis.cs.lappsgrid.stanford.corenlp.api.INamedEntityRecognizer;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.lappsgrid.vocabulary.Annotations;
import org.lappsgrid.vocabulary.Features;
import org.lappsgrid.vocabulary.Metadata;

/**
 * 
 * The Language Application Grid: A Framework for Rapid Adaptation and Reuse
 * <p>
 * Lapps Grid project TODO.
 * <p>
 * @author Chunqi SHI (shicq@cs.brandeis.edu) <br> Jan 31, 2014 </br>
 *
 */
public class NamedEntityRecognizer extends AbstractStanfordCoreNLPWebService
		implements INamedEntityRecognizer {
	public NamedEntityRecognizer() {
		this.init(PROP_TOKENIZE, PROP_SENTENCE_SPLIT, PROP_POS_TAG, PROP_LEMMA, PROP_NER);
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
	public Data execute(Data input) {

        Container container = null;
        try
        {
            container = getContainer(input);
        }
        catch (LappsException e)
        {
            return DataFactory.error(e.getMessage());
        }

        // steps
        ProcessingStep step = new ProcessingStep();
        // steps metadata
        step.getMetadata().put(Metadata.PRODUCED_BY, this.getClass().getName());
        step.getMetadata().put(Metadata.CONTAINS, Annotations.NE);

        //
        IDGenerator id = new IDGenerator();

        // NLP processing
        Annotation annotation = new Annotation(container.getText());
        snlp.annotate(annotation);


        List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
        for (CoreMap sentence1 : sentences) {
            for (CoreLabel token : sentence1.get(TokensAnnotation.class)) {
                org.anc.lapps.serialization.Annotation ann =
                        new org.anc.lapps.serialization.Annotation();
                String ne = token.get(NamedEntityTagAnnotation.class);
                if (! ne.equalsIgnoreCase("O") ){
                    token.setNER(ne);
                }
                ann.setId(id.generate("tok"));
                ann.setStart(token.beginPosition());
                ann.setEnd(token.endPosition());
                ann.setLabel(Annotations.TOKEN);

                Map<String, String> features = ann.getFeatures();
                features.put(Features.LEMMA, token.lemma());
                features.put("category", token.category());
                features.put(Features.PART_OF_SPEECH, token.get(CoreAnnotations.PartOfSpeechAnnotation.class));
                features.put("ner", token.ner());
                features.put("word", token.word());
                step.addAnnotation(ann);
            }
        }
        container.getSteps().add(step);
        return DataFactory.json(container.toJson());
	}

	
	@Override
	public String find(String docs) {		
		Annotation annotation = new Annotation(docs);
		snlp.annotate(annotation);
		
		StringBuffer sb = new StringBuffer();
		
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence1 : sentences) {
			for (CoreLabel token : sentence1.get(TokensAnnotation.class)) {
				String ne = token.get(NamedEntityTagAnnotation.class);
				if ( ne.equalsIgnoreCase("O") ){
					sb.append(token.value());	
				}
				else {
					sb.append("<").append(ne).append(">");
					sb.append(token.value());
					sb.append("</").append(ne).append(">");
				}
				sb.append(" ");
			}
		}
		// return null;
		return sb.substring(0, sb.length() - 1);
	}

}
