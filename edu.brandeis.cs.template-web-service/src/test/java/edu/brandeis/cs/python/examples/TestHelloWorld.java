package edu.brandeis.cs.python.examples;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.brandeis.cs.python.utils.PythonRunner;

public class TestHelloWorld{
	
	
	private HelloWorld hw = null;
	File conf = new File("TestHelloWorld.conf");
	PythonRunner pr = null;
	@Before
	public void setUp() throws Exception {		
		String data = "## \n"+
					"# @file:        lapps.conf\n"+
					"# @brief:        configure file for Python interfaces\n"+
					"##\n"+
					
					"[default]\n"+
					"PythonInterface = lapps_java_cmd.py\n"+
					"PickleHome =\n"+
					
					"[edu.brandeis.cs.python.examples.HelloWorld]\n"+ 
					"PythonFile=examples/helloworld.py \n"+
					"Globals=hello:\"hi\" \n"+
					"Method=say \n"+
					"Args=%%1\n";
							
		FileUtils.writeStringToFile(conf, data, "UTF-8");
		pr = new PythonRunner(conf.getCanonicalPath());
		hw = new HelloWorld(pr);
	}
	
	@Test
	public void testSay() throws Exception{		
		String actual = hw.say("world");
		String expect = "hi world";
		String tips = "";
		Assert.assertEquals(tips, expect, actual);
	}
	
	@Test
	public void testPyroSay() throws Exception{		
		String actual = hw.pyroSay("world").toString();
		String expect = "hi world";
		String tips = "";
		Assert.assertEquals(tips, expect, actual);
		pr.stopPyro4Holder();
	}
	
	@After
	public void tear() throws Exception{
		conf.deleteOnExit();
	}

}
