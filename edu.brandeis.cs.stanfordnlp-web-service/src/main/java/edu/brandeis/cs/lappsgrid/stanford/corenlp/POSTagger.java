package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import java.util.ArrayList;
import java.util.List;

import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;

import edu.brandeis.cs.lappsgrid.stanford.corenlp.api.IPOSTagger;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class POSTagger extends AbstractStanfordCoreNLPWebService implements
		IPOSTagger {

	public POSTagger() {
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
	public Data configure(Data arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data execute(Data input) {
		if (input.getDiscriminator() == Types.ERROR) {
			return input;
		}
		String[] tags = tag(input.getPayload());
		Data data = DataFactory.stringList(tags);
		return data;
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
