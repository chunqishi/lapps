package edu.brandeis.cs.lappsgrid.util;


/**
 * <i>SplitMergeUtil.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 21, 2013<br>
 * 
 */
public class SplitMergeUtil {
	
	public static final String[] fromStrToArr(String s, String splitter) {
		String[] strArr = s.split(splitter);
		return strArr;
	}

	public static final String fromArrToStr(String[] arr,
			String splitter) {
		StringBuilder builder = new StringBuilder(16);

		for (String s : arr) {
			builder.append(s).append(splitter);
		}
		builder.setLength(builder.length() - splitter.length());
		return builder.toString();
	}
	
	
}
