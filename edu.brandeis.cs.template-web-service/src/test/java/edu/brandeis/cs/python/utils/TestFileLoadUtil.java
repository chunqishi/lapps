package edu.brandeis.cs.python.utils;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class TestFileLoadUtil {
	@Test
	public void testLocate()throws Exception{
		File file = FileLoadUtil.locate("lapps.conf");
		System.out.println(file);
		Assert.assertTrue("Not Exist ", file.exists());
	}
	
	
	
}
