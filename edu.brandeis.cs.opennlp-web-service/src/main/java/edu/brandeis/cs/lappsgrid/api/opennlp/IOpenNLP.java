package edu.brandeis.cs.lappsgrid.api.opennlp;


import org.lappsgrid.api.WebService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * <i>IOpenNLP.java</i> Language Application Grids (<b>LAPPS</b>)
 * 
 * <p> OpenNLP command line tool as Web service
 * 
 * <p><b>data</b>: MASC (optional GW, OANC)<br>
 *    <b>spliter</b>: <i><b>OpenNLP</b></i>, Stanford, GATE<br>
 *    <b>tokenizer</b>: <i><b>OpenNLP</b></i>, Stanford, GATE<br>
 *    <b>tagger</b>: <i><b>OpenNLP</b></i>, Stanford, GATE<br>
 *    <b>parser</b>: <i><b>OpenNLP</b></i>, Stanford, GATE NC+VC<br>
 *    <b>named-entity recoginzer (NER)</b>: <i><b>OpenNLP</b></i>, Gate <br> </p>
 * <p><a href="http://opennlp.apache.org/documentation/manual/opennlp.html">Apache OpenNLP Developer Document</a> 
 * <p>Implement interface from {@link opennlp.tools.cmdline.BasicCmdLineTool} 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 19, 2013<br>
 * 
 */
public interface IOpenNLP extends WebService {
//	   protected static final Logger logger = LoggerFactory.getLogger(OpenNLP.class);
}
