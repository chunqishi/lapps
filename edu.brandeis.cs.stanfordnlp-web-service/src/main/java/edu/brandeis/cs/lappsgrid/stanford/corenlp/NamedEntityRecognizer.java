package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import java.util.List;

import org.lappsgrid.api.Data;
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
		this.init("tokenize, ssplit, pos, lemma, ner, parse, dcoref");
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
		String ner = find(input.getPayload());
		Data data = DataFactory.xml(ner);
		return data;
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
