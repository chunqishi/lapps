package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import java.util.ArrayList;
import java.util.List;

import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;

import edu.brandeis.cs.lappsgrid.stanford.corenlp.api.ITokenizer;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class Tokenizer extends AbstractStanfordCoreNLPWebService implements
		ITokenizer {

	public Tokenizer() {
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
		String[] tokens = tokenize(input.getPayload());
		Data data = DataFactory.stringList(tokens);
		return data;
	}

	@Override
	public String[] tokenize(String docs) {
		Annotation annotation = new Annotation(docs);
		snlp.annotate(annotation);
		
		ArrayList<String> list = new ArrayList<String> ();
		
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);
		for (CoreMap sentence1 : sentences) {
			for (CoreLabel token : sentence1.get(TokensAnnotation.class)) {				
				list.add(token.value());
			}
		}
		// return null;
		return list.toArray(new String[list.size()]);
	}


}
