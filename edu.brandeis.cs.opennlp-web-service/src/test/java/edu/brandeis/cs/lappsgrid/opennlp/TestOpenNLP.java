package edu.brandeis.cs.lappsgrid.opennlp;

import java.util.Arrays;

import junit.framework.TestCase;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;

import org.junit.Assert;
import org.junit.Test;

/**
 * <i>TestOpenNLP.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p>
 * <p>
 * Test cases are from <a
 * href="http://www.programcreek.com/2012/05/opennlp-tutorial/">OpenNLP
 * Tutorial</a>
 * <p>
 * 
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>
 *         Nov 20, 2013<br>
 * 
 */
public class TestOpenNLP extends TestCase {

	OpenNLP opennlp;

	public TestOpenNLP() throws OpenNLPWebServiceException {
		opennlp = new OpenNLP();
	}

	@Test
	public void testParser() {
		String[] print = opennlp
				.parserArr(new String[] { "Programcreek is a very huge and useful website." });
		System.out.println(print[0]);
		 String goldPrint =
		 "(TOP (S (NP (NN Programcreek)) (VP (VBZ is) (NP (DT a) (ADJP (RB very) (JJ huge) (CC and) (JJ useful)))) (. website.)))\n";
		 Assert.assertEquals("Parse Failure.", print[0], goldPrint);
	}
	
	
	
	@Test
	public void testTokenizerME() throws OpenNLPWebServiceException {
		String [] tokens  = 
		opennlp.tokenizerME("Hi. How are you? This is Mike.");

		System.out.println(Arrays.toString(tokens));
		String [] goldTokens = {"Hi",".","How","are","you","?","This","is","Mike","."};
		Assert.assertArrayEquals("Tokenize Failure.", goldTokens, tokens);
	}

	@Test
	public void testFind() throws OpenNLPWebServiceException {
//		String tokens[] = SimpleTokenizer.INSTANCE
//				.tokenize("Mike, Smith is a good person and he is from Boston.");
//		System.out.println(Arrays.toString(tokens));
		String[] spans = opennlp.tokenNameFinder("Mike, Smith is a good person and he is from Boston.");
		System.out.println(Arrays.toString(spans));
//		Span[] goldSpans = { new Span(0, 1, "person"), new Span(2, 3, "person"), new Span(11,12,"location") };
//		Assert.assertArrayEquals("NamedEntityRecognizer Failure.", spans,
//				goldSpans);
	}
	

}
