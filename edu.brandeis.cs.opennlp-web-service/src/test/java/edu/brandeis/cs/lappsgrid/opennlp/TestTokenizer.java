package edu.brandeis.cs.lappsgrid.opennlp;

import java.util.Arrays;

import junit.framework.TestCase;
import opennlp.tools.util.Span;

import org.junit.Assert;
import org.junit.Test;

/**
 * <i>TestTokenizer.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> Test cases are from <a href="http://www.programcreek.com/2012/05/opennlp-tutorial/">OpenNLP Tutorial</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class TestTokenizer extends TestCase {
	
	Tokenizer tokenizer;
	
	public TestTokenizer() throws OpenNLPWebServiceException {
		tokenizer = new Tokenizer();
	}
	
	@Test
	public void testTokenize() {
		String [] tokens = tokenizer.tokenize("Hi. How are you? This is Mike.");
		System.out.println(Arrays.toString(tokens));
		String [] goldTokens = {"Hi",".","How","are","you","?","This","is","Mike","."};
		Assert.assertArrayEquals("Tokenize Failure.", goldTokens, tokens);
	}
	
	@Test
	public void testTokenizePos() {
		Span[] boundaries = tokenizer
				.tokenizePos("Hi. How are you? This is Mike.");
		Assert.assertEquals(
				"Tokenize Failure.",
				"[[0..2), [2..3), [4..7), [8..11), [12..15), [15..16), [17..21), [22..24), [25..29), [29..30)]",
				Arrays.toString(boundaries));
	}
}
