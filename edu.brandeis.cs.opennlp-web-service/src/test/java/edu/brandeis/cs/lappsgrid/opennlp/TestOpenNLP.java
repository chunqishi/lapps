package edu.brandeis.cs.lappsgrid.opennlp;

import java.util.Arrays;

import junit.framework.TestCase;

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
 * <a href="https://cwiki.apache.org/confluence/display/OPENNLP/TestPlan1.5.1">Test Plan for Apache OpenNLP 1.5.1</a>
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
	
	/**
	 * testChunkerMET
	 * 
	 * <a href="http://opennlp.apache.org/documentation/1.5.2-incubating/manual/opennlp.html#tools.parser.chunking.cmdline">Chunker Tool</a>
	 * @throws OpenNLPWebServiceException
	 */
	@Test 
	public void testChunkerMET() throws OpenNLPWebServiceException {		
		String [] chunk = opennlp.chunkerMET("Rockwell_NNP International_NNP Corp._NNP 's_POS Tulsa_NNP unit_NN said_VBD it_PRP signed_VBD ._.");
		System.out.println(Arrays.toString(chunk));
		String [] goldChunk = {" [NP Rockwell_NNP International_NNP Corp._NNP ] [NP 's_POS Tulsa_NNP unit_NN ] [VP said_VBD ] [NP it_PRP ] [VP signed_VBD ] ._."};
		Assert.assertArrayEquals("Splitter Failure.", goldChunk, chunk);
	}
	
	@Test
	public void testParser() throws OpenNLPWebServiceException {
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
		String[] finds = opennlp
				.tokenNameFinder("Mike, Smith is a good person and he is from Boston.");
		System.out.println(Arrays.toString(finds));
		String[] goldenArr = new String[] { "<START:person> Mike <END> , <START:person> Smith <END> is a good person and he is from <START:location> Boston <END> ." };
		Assert.assertArrayEquals("Tokenize Failure.", goldenArr, finds);
		
		finds = opennlp
		.tokenNameFinder("Smith is a bad person and he is from Boston.");
		System.out.println(Arrays.toString(finds));
	}
	
//	@Test
//	public void testCoreferencer() throws OpenNLPWebServiceException {
//		String[] refs = opennlp
//				.coreferencer("Mike, Smith is a good person and he is from Boston.\nperson and he is from Boston.");
//		System.out.println("------");
//		System.out.println(Arrays.toString(refs));
//	}
//	
	
	@Test
	public void testSentenceDetector() throws OpenNLPWebServiceException {		
		String [] sents = opennlp.sentenceDetector("Hi. How are you? This is Mike.");
		System.out.println(Arrays.toString(sents));
		String [] goldSents = {"Hi. How are you?","This is Mike."};
		Assert.assertArrayEquals("Splitter Failure.", goldSents, sents);
	}

}
