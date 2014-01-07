package edu.brandeis.cs.python.utils;

import junit.framework.Assert;

import org.junit.Test;

public class TestFileLoadUtil {
	@Test
	public void testLocate()throws Exception{
		System.out.println(FileLoadUtil.locate("lapps.conf.template"));
	}
	
	
	
}
