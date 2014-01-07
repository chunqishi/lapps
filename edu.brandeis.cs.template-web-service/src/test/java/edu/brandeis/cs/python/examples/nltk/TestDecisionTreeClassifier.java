package edu.brandeis.cs.python.examples.nltk;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.brandeis.cs.python.utils.PythonRunner;

public class TestDecisionTreeClassifier{
	
	
	private DecisionTreeClassifier dtc = null;
	

	@Before
	public void setUp() throws Exception {		
		dtc = new DecisionTreeClassifier();
	}
	
//	@Test
//	public void testTrain() throws Exception{
//		String actual = dtc.train("features_brown_news");
//		String expect = "";
//		String tips = "";
//		Assert.assertEquals(tips, expect, actual);
//	}
	
	@Test
	public void testPredict() throws Exception{
		String actual = dtc.predict("features_brown_news", "boy");
		String expect = "IN";
		String tips = "Boy has wrong tagger";
		Assert.assertEquals(tips, expect, actual);
	}
}
