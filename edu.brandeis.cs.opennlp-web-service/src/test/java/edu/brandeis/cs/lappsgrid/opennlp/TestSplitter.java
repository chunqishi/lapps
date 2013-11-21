package edu.brandeis.cs.lappsgrid.opennlp;

import java.util.Arrays;

import junit.framework.TestCase;
import opennlp.tools.util.Span;

import org.junit.Assert;
import org.junit.Test;

/**
 * <i>TestSplitter.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> Test cases are from <a href="http://www.programcreek.com/2012/05/opennlp-tutorial/">OpenNLP Tutorial</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class TestSplitter extends TestCase {
	
	Splitter splitter;
	
	public TestSplitter() throws OpenNLPWebServiceException {
		splitter = new Splitter();
	}
	
	@Test
	public void testSentDetect() {
		String [] sents = splitter.sentDetect("Hi. How are you? This is Mike.");
		System.out.println(Arrays.toString(sents));
		String [] goldSents = {"Hi. How are you?","This is Mike."};
		Assert.assertArrayEquals("Splitter Failure.", goldSents, sents);
	}
	
	@Test
	public void testSentDetectPos() {
		Span[] offsets = splitter
				.sentPosDetect("Hi. How are you? This is Mike.");
		System.out.println(Arrays.toString(offsets));
		Assert.assertEquals(
				"Splitter Failure.",
				"[[0..16), [17..30)]",
				Arrays.toString(offsets));
	}
}
