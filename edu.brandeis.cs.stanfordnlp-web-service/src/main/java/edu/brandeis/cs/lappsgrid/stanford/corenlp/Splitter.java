package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import java.util.ArrayList;
import java.util.List;

import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;

import edu.brandeis.cs.lappsgrid.stanford.corenlp.api.ISplitter;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class Splitter extends AbstractStanfordCoreNLPWebService implements
		ISplitter {

	public Splitter() {
		super();
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
		if (input.getDiscriminator() == Types.ERROR) {
			return input;
		}
		String [] sents = split(input.getPayload());
		Data data = DataFactory.stringList(sents);
		return data;
	}

	@Override
	public String[] split(String docs) {
		Annotation annotation = new Annotation(docs);
		snlp.annotate(annotation);
		
		ArrayList<String> list = new ArrayList<String> ();
		
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence1 : sentences) {
			System.out.println(sentence1);
			list.add(sentence1.toString());
		}
		// return null;
		return list.toArray(new String[list.size()]);
	}

}
