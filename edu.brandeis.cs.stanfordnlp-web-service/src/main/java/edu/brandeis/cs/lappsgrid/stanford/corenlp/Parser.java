package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import org.anc.lapps.serialization.Container;
import org.anc.lapps.serialization.ProcessingStep;
import org.anc.util.IDGenerator;
import org.lappsgrid.api.Data;
import org.lappsgrid.api.LappsException;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;

import edu.brandeis.cs.lappsgrid.stanford.corenlp.api.IParser;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import org.lappsgrid.vocabulary.Annotations;
import org.lappsgrid.vocabulary.Features;
import org.lappsgrid.vocabulary.Metadata;

public class Parser extends AbstractStanfordCoreNLPWebService implements
		IParser {

	public Parser() {
        this.init(PROP_TOKENIZE, PROP_SENTENCE_SPLIT, PROP_PARSE);
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
        step.getMetadata().put(Metadata.PRODUCED_BY, this.getClass().getName() );


        //
        IDGenerator id = new IDGenerator();


        // NLP processing
        Annotation annotation = new Annotation(container.getText());
        snlp.annotate(annotation);
        List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
        for (CoreMap sentence1 : sentences) {
            for (Tree tree : sentence1.get(TreeAnnotation.class)) {
                org.anc.lapps.serialization.Annotation ann =
                        new org.anc.lapps.serialization.Annotation();
                StringWriter sw = new StringWriter();
                PrintWriter writer = new PrintWriter(sw);
                tree.printLocalTree(writer);
                ann.setId(id.generate("tok"));
                ann.setLabel(Annotations.TOKEN);
                Map<String, String> features = ann.getFeatures();
                features.put("label", tree.label().value());
                features.put("tree", sw.toString());
                step.addAnnotation(ann);
            }
        }

        container.getSteps().add(step);
        return DataFactory.json(container.toJson());
	}

	@Override
	public String parse(String docs) {
		Annotation annotation = new Annotation(docs);
		snlp.annotate(annotation);
		
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);  
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence1 : sentences) {
			for (Tree tree : sentence1.get(TreeAnnotation.class)) {				
				tree.printLocalTree(writer);
			}
		}
		// return null;
		return sw.toString();
	}

}
