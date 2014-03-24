package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.anc.lapps.serialization.Container;
import org.anc.lapps.serialization.ProcessingStep;
import org.anc.util.IDGenerator;
import org.lappsgrid.api.Data;
import org.lappsgrid.api.LappsException;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;

import edu.brandeis.cs.lappsgrid.stanford.corenlp.api.IPOSTagger;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import org.lappsgrid.vocabulary.Annotations;
import org.lappsgrid.vocabulary.Features;
import org.lappsgrid.vocabulary.Metadata;


public class POSTagger extends AbstractStanfordCoreNLPWebService implements
		IPOSTagger {

	public POSTagger() {
        this.init(PROP_TOKENIZE, PROP_SENTENCE_SPLIT, PROP_POS_TAG);
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
	public Data configure(Data arg0) {
		// TODO Auto-generated method stub
		return null;
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
        step.getMetadata().put(Metadata.PRODUCED_BY, this.getClass().getName()  + ":" + Version);
        step.getMetadata().put(Metadata.CONTAINS, Features.PART_OF_SPEECH);

        //
        IDGenerator id = new IDGenerator();


        // NLP processing
        Annotation annotation = new Annotation(container.getText());
        snlp.annotate(annotation);
        List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
        for (CoreMap sentence1 : sentences) {
            for (CoreLabel token : sentence1.get(TokensAnnotation.class)) {
                String ps = token.get(PartOfSpeechAnnotation.class);
                org.anc.lapps.serialization.Annotation ann =
                        new org.anc.lapps.serialization.Annotation();
                ann.setId(id.generate("tok"));
                ann.setStart(token.beginPosition());
                ann.setEnd(token.endPosition());
                ann.setLabel(Annotations.TOKEN);

                Map<String, String> features = ann.getFeatures();
                putFeature(features, Features.LEMMA, token.lemma());
                putFeature(features, Features.CATEGORY, token.category());
                putFeature(features, Features.PART_OF_SPEECH, token.get(PartOfSpeechAnnotation.class));
                putFeature(features, Features.NER, token.ner());
                putFeature(features, Features.WORD, token.word());

                step.addAnnotation(ann);
            }
        }
        container.getSteps().add(step);
        return DataFactory.json(container.toJson());
	}

	@Override
	public String[] tag(String docs) {
		Annotation annotation = new Annotation(docs);
		snlp.annotate(annotation);
		
		ArrayList<String> list = new ArrayList<String> ();
		
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence1 : sentences) {
			for (CoreLabel token : sentence1.get(TokensAnnotation.class)) {
				String ps = token.get(PartOfSpeechAnnotation.class);
				list.add(ps);
			}
		}
		// return null;
		return list.toArray(new String[list.size()]);
	}


}
