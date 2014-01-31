package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import edu.brandeis.cs.lappsgrid.stanford.StanfordWebServiceException;
import edu.brandeis.cs.lappsgrid.stanford.corenlp.Parser;

/**
 * <i>TestParser.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> Test cases are from <a href="http://www.programcreek.com/2012/05/opennlp-tutorial/">OpenNLP Tutorial</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class TestParser extends TestCase {
	
	Parser parser;
	
	public TestParser() throws StanfordWebServiceException {
		parser = new Parser();
	}
	
	@Test
	public void testParser() {
		String print = parser.parse("Programcreek is a very huge and useful website.");
		System.out.println(print);
		String goldPrint = "(ROOT (S) )"
				+ "(S (NP) (VP) (.) )"
				+ "(NP (NNP) )"
				+ "(NNP (Programcreek-1) )"
				+ "(Programcreek-1 )"
				+ "(VP (VBZ) (NP) )"
				+ "(VBZ (is-2) )"
				+ "(is-2 )"
				+ "(NP (DT) (ADJP) (NN) )"
				+ "(DT (a-3) )"
				+ "(a-3 )"
				+ "(ADJP (RB) (JJ) (CC) (JJ) )"
				+ "(RB (very-4) )"
				+ "(very-4 )"
				+ "(JJ (huge-5) )"
				+ "(huge-5 )"
				+ "(CC (and-6) )"
				+ "(and-6 )"
				+ "(JJ (useful-7) )"
				+ "(useful-7 )"
				+ "(NN (website-8) )"
				+ "(website-8 )"
				+ "(. (.-9) )"
				+ "(.-9 )";
		Assert.assertEquals("Parse Failure.", print.replace("\n",""), goldPrint);
	}

}
