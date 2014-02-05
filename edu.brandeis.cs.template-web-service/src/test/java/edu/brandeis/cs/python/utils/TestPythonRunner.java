package edu.brandeis.cs.python.utils;

import java.io.File;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.brandeis.cs.python.examples.HelloWorld;


/**
 * @brief Test main functions
 * @author shicq@cs.brandeis.edu
 *
 */
public class TestPythonRunner {
	PythonRunner pr = null;
	String confPath = "TestPythonRunner.conf";
	
	@Before
	public void setUp() throws Exception
    {
		String confContent = "# This is example case of TestPythonRunner configuration \r\n"
				+ "[example]  \r\n"
				+ " Loads=objName:pickleId, objName2:pickleId2 \r\n" 
				+ " Globals=gVariable:objName2 \r\n"
				+ " PythonFile=examples/helloworld.py \r\n"
				+ " Method=append \r\n"
				+ " Args=objName, objName2 \r\n"
				+ " Return=objName3 \r\n"
				+ " Dumps=objName3:subDirectory/pickId3 \r\n"
				+ " \r\n"
				+ " [examples.workflow] \r\n"
				+ " Requires=example,example \r\n"
				+ " \r\n"
				+ " [" + PythonRunner.CONF_SECTION_DEFAULT + "] \r\n"
				+ " PythonFile=lapps_common_io.py \r\n";
		File confFile = new File(confPath);
		System.out.println("Path=" + confFile.getAbsolutePath());
		FileUtils.writeStringToFile(confFile, confContent, "UTF-8");
		pr = new PythonRunner(confPath);
    }
	
	@Test
	public void testGetSectionProperty()throws Exception{
		String actual = pr.getSectionProperty("example", "Loads");
		Assert.assertEquals("Fail: read section=example", "objName:pickleId, objName2:pickleId2", actual);
		actual = pr.getSectionProperty("example", "Load");
		Assert.assertEquals("Fail provide default value", "", actual);
		actual = pr.getSectionProperty("examples.workflow", "Requires");
		Assert.assertEquals("Fail: read section=examples.workflow", "example,example", actual);
	}
	
	@After
	public void tearDown() throws Exception {
		File confFile = new File(confPath);
		confFile.delete();
	}
	
	
	
} 