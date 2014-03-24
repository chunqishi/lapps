package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import edu.brandeis.cs.lappsgrid.stanford.StanfordWebServiceException;
import edu.brandeis.cs.lappsgrid.stanford.corenlp.Tokenizer;


/**
 * <i>TestTokenizer.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> Test cases are from <a href="http://www.programcreek.com/2012/05/opennlp-tutorial/">OpenNLP Tutorial</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class TestTokenizer extends TestService {
	
	Tokenizer tokenizer;
	
	public TestTokenizer() throws StanfordWebServiceException {
		tokenizer = new Tokenizer();
	}
	
	@Test
	public void testTokenize() {
		String [] tokens = tokenizer.tokenize("Hi. How are you? This is Mike.");
//		System.out.println(Arrays.toString(tokens));
		String [] goldTokens = {"Hi",".","How","are","you","?","This","is","Mike","."};
		Assert.assertArrayEquals("Tokenize Failure.", goldTokens, tokens);
	}

    @Test
    public void testExecute(){
        ret = tokenizer.execute(data);
        Assert.assertTrue(ret.getPayload().contains("by return email or by telephone"));
        System.out.println(ret.getPayload());
    }
}
