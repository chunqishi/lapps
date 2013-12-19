package edu.brandeis.cs.python.examples;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.brandeis.cs.python.utils.PythonRunner;

public class TestHelloWorld{
	
	
	private HelloWorld hw = null;
	

	@Before
	public void setUp() throws Exception {		
		hw = new HelloWorld();
	}
	
	@Test
	public void testSay() throws Exception{
		HelloWorld hw = new HelloWorld();
		String actual = hw.say("world");
		String expect = "";
		String tips = "";
		Assert.assertEquals(tips,expect, actual);
		PythonRunner pr = new PythonRunner();
		String s = pr.runPythonSection("helloworld.append", "hello", "world");
	}

}
