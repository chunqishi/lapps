package edu.brandeis.cs.lappsgrid.api.opennlp;

import opennlp.tools.namefind.TokenNameFinder;

import org.lappsgrid.api.WebService;

/**
 * <i>INamedEntityRecoginizer.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> <a href="http://opennlp.apache.org/documentation/manual/opennlp.html#tools.namefind.recognition">Named Entity Recognition</a>
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public interface INamedEntityRecoginizer extends TokenNameFinder, WebService {

}
