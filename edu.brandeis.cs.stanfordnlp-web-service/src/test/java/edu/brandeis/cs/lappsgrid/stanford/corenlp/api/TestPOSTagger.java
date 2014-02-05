package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import edu.brandeis.cs.lappsgrid.stanford.StanfordWebServiceException;
import edu.brandeis.cs.lappsgrid.stanford.corenlp.POSTagger;

/**
 * <i>TestPOSTagger.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> Test cases are from <a href="http://www.programcreek.com/2012/05/opennlp-tutorial/">OpenNLP Tutorial</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class TestPOSTagger extends TestCase {
	
	POSTagger postagger;
	
	public TestPOSTagger() throws StanfordWebServiceException {
		postagger = new POSTagger();
	}
	
	@Test
	public void testTokenize() {
		String docs = "Hi. How are you? This is Mike.";
		String[] tags = postagger.tag(docs);
		System.out.println(Arrays.toString(tags));
		String [] goldTags = {"NN", ".", "WRB", "VBP", "PRP", ".", "DT", "VBZ", "NNP", "."};
		Assert.assertArrayEquals("Tokenize Failure.", goldTags, tags);
	}

}
