package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import edu.brandeis.cs.lappsgrid.stanford.StanfordWebServiceException;
import edu.brandeis.cs.lappsgrid.stanford.corenlp.NamedEntityRecognizer;

/**
 * <i>TestTokenizer.java</i> Language Application Grids (<b>LAPPS</b>)
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
public class TestNamedEntityRecognizer extends TestService {

	NamedEntityRecognizer ner;

	public TestNamedEntityRecognizer() throws StanfordWebServiceException {
		ner = new NamedEntityRecognizer();
	}

	@Test
	public void testFind() {
		String text = "Mike, Smith is a good person and he is from Boston.";
		String ners = ner.find(text);
//		System.out.println(ners);
		Assert.assertEquals(
				"NamedEntityRecognizer Failure.",
				ners,
				"<PERSON>Mike</PERSON> , <PERSON>Smith</PERSON> is a good person and he is from <LOCATION>Boston</LOCATION> .");
	}




    @Test
    public void testExecute(){
        ret = ner.execute(data);
        Assert.assertTrue(ret.getPayload().contains("PERSON"));
        Assert.assertTrue(ret.getPayload().contains("by return email or by telephone"));
    }

}
