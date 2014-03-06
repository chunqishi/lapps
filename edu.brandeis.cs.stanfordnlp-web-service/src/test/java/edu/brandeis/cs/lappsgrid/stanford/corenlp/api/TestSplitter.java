package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import edu.brandeis.cs.lappsgrid.stanford.StanfordWebServiceException;
import edu.brandeis.cs.lappsgrid.stanford.corenlp.Splitter;

/**
 * <i>TestSplitter.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> Test cases are from <a href="http://www.programcreek.com/2012/05/opennlp-tutorial/">OpenNLP Tutorial</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class TestSplitter extends TestService {
	
	Splitter splitter;
	
	public TestSplitter() throws StanfordWebServiceException {
		splitter = new Splitter();
	}
	
	@Test
	public void testSplit() {
		String [] sents = splitter.split("Hi. How are you? This is Mike.");
//		System.out.println(Arrays.toString(sents));
		String [] goldSents = {"Hi.","How are you?","This is Mike."};
		Assert.assertArrayEquals("Splitter Failure.", goldSents, sents);
	}


    @Test
    public void testExecute(){
        ret = splitter.execute(data);
        Assert.assertTrue(ret.getPayload().contains("by return email or by telephone"));
    }
}
