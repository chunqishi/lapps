package edu.brandeis.cs.lappsgrid.stanford.corenlp.api;


import org.lappsgrid.api.WebService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.lappsgrid.discriminator.Types;

import edu.brandeis.cs.lappsgrid.stanford.StanfordWebServiceException;

/**
 * <i>IOpenNLP.java</i> Language Application Grids (<b>LAPPS</b>)
 * 
 * <p> OpenNLP command line tool as Web service
 * 
 * <p><b>data</b>: MASC (optional GW, OANC)<br>
 *    <b>spliter</b>:OpenNLP,  <i><b>Stanford</b></i>, GATE<br>
 *    <b>tokenizer</b>: OpenNLP,  <i><b>Stanford</b></i>, GATE<br>
 *    <b>tagger</b>: OpenNLP,  <i><b>Stanford</b></i>, GATE<br>
 *    <b>parser</b>: OpenNLP,  <i><b>Stanford</b></i>, GATE NC+VC<br>
 *    <b>named-entity recoginzer (NER)</b>: OpenNLP, <i><b>Stanford</b></i>, Gate <br> </p>
 * <p><a href="http://nlp.stanford.edu/software/corenlp.shtml">Stanford Developer Document</a> 
 * <p>Implement interface
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 19, 2013<br>
 * 
 */
public interface IStanford extends WebService {
	public static final long [] TYPES_REQUIRES = new long[] { Types.STANFORD, Types.TEXT };
	public static final long [] TYPES_PRODUCES = new long[] { Types.STANFORD, Types.SENTENCE };
	public static final String FILE_PROPERTIES = "opennlp-web-service.properties";
	
	public static final String SPLITTER_LINE = "\n";
	
	/**
	 * chunkerMET
	 * @see opennlp.tools.cmdline.BasicCmdLineTool
	 * @see opennlp.tools.cmdline.chunker.ChunkerConverterTool
	 */
	public String[] chunkerMETArr(String[] lines) throws StanfordWebServiceException;
	
	/**
	 * 
	 * @param lines: splitter mark is "\n" {@link SPLITTER_LINE} 
	 * @return
	 * @throws OpenNLPWebServiceException
	 */
	public String[] chunkerMET(String lineswithsplitter) throws StanfordWebServiceException;
	
	/**
	 * Coreferencer
	 * @see opennlp.tools.cmdline.BasicCmdLineTool
	 * @see opennlp.tools.cmdline.coref.CoreferencerTool
	 */
//	public String[] coreferencerArr(String[] lines) throws OpenNLPWebServiceException;
//	
//	public String[] coreferencer(String lineswithsplitter) throws OpenNLPWebServiceException;
	
	/**
	 * DictionaryDetokenizer
	 * @see opennlp.tools.cmdline.BasicCmdLineTool
	 * @see opennlp.tools.cmdline.tokenizer.DictionaryDetokenizerTool
	 */
//	public String[] dictionaryDetokenizerArr(String[] lines) throws OpenNLPWebServiceException;
//	
//	public String[] dictionaryDetokenizer(String lineswithsplitter) throws OpenNLPWebServiceException;
	
	/**
	 * Doccat
	 * @see opennlp.tools.cmdline.BasicCmdLineTool
	 * @see opennlp.tools.cmdline.doccat.DoccatTool
	 */
//	public String[] doccatArr(String[] lines) throws OpenNLPWebServiceException;
//	
//	public String[] doccat(String lineswithsplitter) throws OpenNLPWebServiceException;
	
	/**
	 * Parser
	 * @see opennlp.tools.cmdline.BasicCmdLineTool
	 * @see opennlp.tools.cmdline.parser.ParserTool
	 */
	public String[] parserArr(String[] lines) throws StanfordWebServiceException;
	
	public String[] parser(String lineswithsplitter) throws StanfordWebServiceException;
	
	
	/**
	 * SentenceDetector
	 * @see opennlp.tools.cmdline.BasicCmdLineTool
	 * @see opennlp.tools.cmdline.sentdetect.SentenceDetectorTool
	 */
	public String[] sentenceDetectorArr(String[] lines) throws StanfordWebServiceException;
	
	public String[] sentenceDetector(String lineswithsplitter) throws StanfordWebServiceException;
	
	
	/**
	 * SimpleTokenizer
	 * @see opennlp.tools.cmdline.BasicCmdLineTool
	 * @see opennlp.tools.cmdline.tokenizer.SimpleTokenizerTool
	 */
	public String[] simpleTokenizerArr(String[] lines) throws StanfordWebServiceException;
	
	public String[] simpleTokenizer(String lineswithsplitter) throws StanfordWebServiceException;
	
	/**
	 * TokenizerME
	 * @see opennlp.tools.cmdline.BasicCmdLineTool
	 * @see opennlp.tools.cmdline.tokenizer.TokenizerMETool
	 */
	public String[] tokenizerMEArr(String[] lines) throws StanfordWebServiceException;
	
	public String[] tokenizerME(String lineswithsplitter) throws StanfordWebServiceException;
	
	/**
	 * TokenNameFinder
	 * @see opennlp.tools.cmdline.BasicCmdLineTool
	 * @see opennlp.tools.cmdline.namefind.TokenNameFinderTool
	 */
	public String[] tokenNameFinderArr(String[] lines) throws StanfordWebServiceException;
	
	public String[] tokenNameFinder(String lineswithsplitter) throws StanfordWebServiceException;
}
