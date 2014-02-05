package edu.brandeis.cs.lappsgrid.stanford.corenlp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;

import edu.brandeis.cs.lappsgrid.stanford.corenlp.api.IParser;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class Parser extends AbstractStanfordCoreNLPWebService implements
		IParser {

	public Parser() {
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
		String res = parse(input.getPayload());
		Data data = DataFactory.text(res);
		return data;
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
//				String ne = token.get(TreeAnnotation.class);
//				if ( ne.equalsIgnoreCase("O") ){
//					sb.append(token.value());	
//				}
//				else {
//					sb.append("<").append(ne).append(">");
//					sb.append(token.value());
//					sb.append("</").append(ne).append(">");
//				}
//				sb.append(" ");
			}
		}
		// return null;
		return sw.toString();
	}

}
