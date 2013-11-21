package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import opennlp.tools.chunker.ChunkSample;
import opennlp.tools.chunker.Chunker;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.chunker.ChunkerModelLoader;
import opennlp.tools.cmdline.namefind.TokenNameFinderModelLoader;
import opennlp.tools.cmdline.parser.ParserModelLoader;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.tokenizer.TokenizerModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.DetokenizationDictionary;
import opennlp.tools.tokenize.Detokenizer;
import opennlp.tools.tokenize.DictionaryDetokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import org.anc.resource.ResourceLoader;
import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.brandeis.cs.lappsgrid.api.opennlp.IOpenNLP;
import edu.brandeis.cs.lappsgrid.util.SplitMergeUtil;

/**
 * <i>OpenNLP.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p><a href="http://opennlp.sourceforge.net/models-1.5/">Models for 1.5 series</a>
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 20, 2013<br>
 * 
 */
public class OpenNLP implements IOpenNLP {
    protected static final Logger logger = LoggerFactory.getLogger(OpenNLP.class);
    
    
	public OpenNLP() throws OpenNLPWebServiceException {
		init();
	}
	
	private Properties prop = new Properties();
	
	private static final ClassLoader getClassLoader() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = ResourceLoader.class.getClassLoader();
		}
		return loader;
	}
    
	protected void init() throws OpenNLPWebServiceException {	
		logger.info("init(): Creating OpenNLP ...");

		InputStream stream = ResourceLoader.open(FILE_PROPERTIES);
		if (stream == null) {
			logger.error("init(): fail to open \""+ FILE_PROPERTIES + "\".");
			throw new OpenNLPWebServiceException(
					"init(): fail to open \""+ FILE_PROPERTIES + "\".");
		}		
		
		try {
			prop.load(stream);
			stream.close();
		} catch (IOException e) {
			logger.error("init(): fail to load \""+ FILE_PROPERTIES + "\".");
			throw new OpenNLPWebServiceException(
					"init(): fail to load \""+ FILE_PROPERTIES + "\".");
		}

		logger.info("init(): load "+ FILE_PROPERTIES + ".");
		
		// default English
//		String nerModels = prop.getProperty(PROP_COMPNENT_MODEL,
//				"en-ner-person.bin");


//		for (String nerModel : nerModels.split(":")) {
//			logger.info("init(): load " + nerModel + " ...");
//			if (nerModel.trim().length() > 0) {
//				TokenNameFinder nameFinder = load(nerModel);
//				if (nameFinder != null)
//					nameFinders.add(nameFinder);
//			}
//		}

		logger.info("init(): Creating OpenNLP !");
	}
	
	

	@Override
	public Data configure(Data data) {
		return DataFactory.ok();
	}

	@Override
	public Data execute(Data data) {
		logger.info("execute(): Execute OpenNLP tokenizer ...");
//
//		if (tokenizer == null) {
//			try {
//				init();
//			} catch (OpenNLPWebServiceException e) {
//				logger.error("execute(): Fail to initialize Tokenizer");
//				return DataFactory
//						.error("execute(): Fail to initialize Tokenizer");
//			}
//		}
//
//		String[] tokens = tokenize(data.getPayload());
//		logger.info("execute(): Execute OpenNLP tokenizer!");
//		return DataFactory.stringList(tokens);
		return null;
	}
	
	@Override
	public long[] requires() {
		return TYPES_REQUIRES;
	}

	@Override
	public long[] produces() {
		return TYPES_PRODUCES;
	}

	@Override
	public String[] chunkerMETArr(String[] lines) {
		// default English
		String nerModels = prop.getProperty("Chunker", "en-chunker.bin");
		logger.info("chunkerMET(): load " + nerModels + ".");

		ArrayList<Chunker> chunkers = new ArrayList<Chunker>();

		for (String nerModel : nerModels.split(":")) {
			logger.info("init(): load " + nerModel + " ...");
			if (nerModel.trim().length() > 0) {
				ChunkerModel model = new ChunkerModelLoader().load(locate(
						nerModel));
				ChunkerME chunker = new ChunkerME(model,
						ChunkerME.DEFAULT_BEAM_SIZE);

				if (chunker != null)
					chunkers.add(chunker);
			}
		}
		ArrayList<String> chunkArr = new ArrayList<String>();

		for (String line : lines) {

			POSSample posSample;
			try {
				posSample = POSSample.parse(line);
			} catch (InvalidFormatException e) {
				System.err.println("Invalid format:");
				System.err.println(line);
				logger.error("chunkerMET(): Invalid format:" + line);
				continue;
			}

			for (Chunker chunker : chunkers) {
				String[] chunks = chunker.chunk(posSample.getSentence(),
						posSample.getTags());
				String chunk = new ChunkSample(posSample.getSentence(),
						posSample.getTags(), chunks).nicePrint();
				chunkArr.add(chunk);
				System.out.println(chunk);
			}
		}
		return chunkArr.toArray(new String[chunkArr.size()]);
	}

	@Override
	public String[] coreferencerArr(String[] lines) {
		
		// default English
		String nerModels = prop.getProperty("Coreference",
				"coref");
		logger.info("coreferencer(): load "+ nerModels + ".");

//		ArrayList<TreebankLinker> chunkers = new ArrayList<TreebankLinker>();
//		
//		for (String nerModel : nerModels.split(":")) {
//			logger.info("init(): load " + nerModel + " ...");
//			if (nerModel.trim().length() > 0) {
//			      TreebankLinker treebankLinker;
//			      try {
//			        treebankLinker = new TreebankLinker (nerModel, LinkerMode.TEST);
//			      } catch (IOException e) {
//			        throw new TerminateToolException(-1, "Failed to load all coreferencer models!", e);
//			      }			  
//
//				if (treebankLinker != null)
//					chunkers.add(treebankLinker);
//			}
//		}
//
//
//	      
//	      try {
//	        
//	        int sentenceNumber = 0;
//	        List<Mention> document = new ArrayList<Mention>();
//	        List<Parse> parses = new ArrayList<Parse>();
//	        
//			ArrayList<String> arr = new ArrayList<String>();
//			
//			for (TreebankLinker treebankLinker:chunkers)
//
//	        for (String line : lines) {
//
//	          if (line.equals("")) {
//	            DiscourseEntity[] entities = treebankLinker.getEntities(document.toArray(new Mention[document.size()]));
//	            //showEntities(entities);
//	            new CorefParse(parses,entities).show();
//	            sentenceNumber=0;
//	            document.clear();
//	            parses.clear();
//	          }
//	          else {
//	            Parse p = Parse.parseParse(line);
//	            parses.add(p);
//	            Mention[] extents = treebankLinker.getMentionFinder().getMentions(new DefaultParse(p,sentenceNumber));
//	            //construct new parses for mentions which don't have constituents.
//	            for (int ei=0,en=extents.length;ei<en;ei++) {
//	              //System.err.println("PennTreebankLiner.main: "+ei+" "+extents[ei]);
//
//	              if (extents[ei].getParse() == null) {
//	                //not sure how to get head index, but its not used at this point.
//	                Parse snp = new Parse(p.getText(),extents[ei].getSpan(),"NML",1.0,0);
//	                p.insert(snp);
//	                extents[ei].setParse(new DefaultParse(snp,sentenceNumber));
//	              }
//
//	            }
//	            document.addAll(Arrays.asList(extents));
//	            sentenceNumber++;
//	          }
//
//	        }
//	      }
//	      catch (IOException e) {
//	        CmdLineUtil.handleStdinIoError(e);
//	      }
		return null;
	}

	@Override
	public String[] dictionaryDetokenizerArr(String[] lines) throws OpenNLPWebServiceException {
		
		String nerModels = prop.getProperty("Chunker",
				"en-chunker.bin");
		logger.info("dictionaryDetokenizer(): load "+ nerModels + ".");

		ArrayList<Detokenizer> chunkers = new ArrayList<Detokenizer>();
		
		for (String nerModel : nerModels.split(":")) {
			logger.info("dictionaryDetokenizer(): load " + nerModel + " ...");
			if (nerModel.trim().length() > 0) {
				
				
				InputStream stream = ResourceLoader.open(nerModel);
				if (stream == null) {
					logger.error("dictionaryDetokenizer(): fail to open PARSER MODEl \"" + nerModel
							+ "\".");
					throw new OpenNLPWebServiceException(
							"dictionaryDetokenizer(): fail to open PARSER MODEl \"" + nerModel + "\".");
				}
				try {
					try {
						Detokenizer detokenizer = new DictionaryDetokenizer(
								new DetokenizationDictionary(stream));
						if (detokenizer != null)
							chunkers.add(detokenizer);
					} finally {
						stream.close();
					}
				} catch (InvalidFormatException ife) {
					throw new OpenNLPWebServiceException(
							"dictionaryDetokenizer(): invalid format \"" + nerModel
									+ "\".");
				} catch (IOException ioe) {
					throw new OpenNLPWebServiceException(
							"dictionaryDetokenizer(): close exception \"" + nerModel
									+ "\".");
				}
			}
		}
		ArrayList<String> arr = new ArrayList<String>();

		for(Detokenizer detokenizer:chunkers)
	        for (String tokenizedLine:lines) {

	          // white space tokenize line
	          String tokens[] = WhitespaceTokenizer.INSTANCE.tokenize(tokenizedLine);

	          System.out.println(detokenizer.detokenize(tokens, null));
	          
	          arr.add(detokenizer.detokenize(tokens, null));

	        }
	        

		return arr.toArray(new String[arr.size()]);
	}

	@Override
	public String[] doccatArr(String[] lines) throws OpenNLPWebServiceException {
		throw new OpenNLPWebServiceException(
				"doccat(): to be implemented exception");
	}
	
	
	protected static File locate(String resource) {
		File resFile;
		try {
			resFile = new File (getClassLoader() .getResource(resource).toURI());
			System.out.println(resFile);
		} catch (URISyntaxException e) {
			resFile = new File (resource);
			e.printStackTrace();
		}
		return resFile;
	}

	
	@Override
	public String[] parserArr(String[] lines) {		
		String nerModels = prop.getProperty(Parser.PROP_COMPNENT_MODEL,
				"en-parser-chunking.bin");
		logger.info("parser(): load "+ nerModels + ".");
		
		ArrayList<opennlp.tools.parser.Parser> chunkers = new ArrayList<opennlp.tools.parser.Parser>();
		
		for (String nerModel : nerModels.split(":")) {
			logger.info("parser(): load " + nerModel + " ...");
			if (nerModel.trim().length() > 0){
			ParserModel model = new ParserModelLoader().load(locate(nerModel));
			opennlp.tools.parser.Parser parser =
	          ParserFactory.create(model);
			chunkers.add(parser);
			}
		}

		ArrayList<String> arr = new ArrayList<String>();

		for (opennlp.tools.parser.Parser parser:chunkers)
	        for (String tokenizedLine:lines) {
	    		StringBuffer builder = new StringBuffer();
	    		opennlp.tools.parser.Parse [] parses = ParserTool.parseLine(tokenizedLine, parser, 1);

	    		for (int pi = 0, pn = parses.length; pi < pn; pi++) {
	    			parses[pi].show(builder);
	    			builder.append("\n");
	    		}

	    		arr.add( builder.toString());
	        }

		return arr.toArray(new String[arr.size()]);
	}
	
	
	@Override
	public String[] sentenceDetectorArr(String[] lines)
			throws OpenNLPWebServiceException {
		String nerModels = prop.getProperty(Splitter.PROP_COMPNENT_MODEL,
				"en-sent.bin");
		logger.info("sentenceDetector(): load " + nerModels + ".");

		ArrayList<SentenceDetector> chunkers = new ArrayList<SentenceDetector>();

		for (String nerModel : nerModels.split(":")) {
			logger.info("sentenceDetector(): load " + nerModel + " ...");
			if (nerModel.trim().length() > 0) {
				InputStream stream = ResourceLoader.open(nerModel);
				if (stream == null) {
					logger.error("sentenceDetector(): fail to open SENTENCE MODEl \""
							+ nerModel + "\".");
					throw new OpenNLPWebServiceException(
							"sentenceDetector(): fail to open SENTENCE MODEl \""
									+ nerModel + "\".");
				}
				try {
					try {
						SentenceModel model = new SentenceModel(stream);
						SentenceDetectorME sdetector = new SentenceDetectorME(
								model);
						chunkers.add(sdetector);
					} finally {
						stream.close();
					}
				} catch (InvalidFormatException ife) {
					throw new OpenNLPWebServiceException(
							"sentenceDetector(): invalid format \"" + nerModel
									+ "\".");
				} catch (IOException ioe) {
					throw new OpenNLPWebServiceException(
							"sentenceDetector(): close exception \"" + nerModel
									+ "\".");
				}
			}
		}

		ArrayList<String> arr = new ArrayList<String>();
		StringBuffer builder = new StringBuffer();
		for (SentenceDetector sdetector : chunkers)
			for (String tokenizedLine : lines) {
				String[] sents = sdetector.sentDetect(tokenizedLine);
				builder.setLength(0);
				for (String sentence : sents) {
					System.out.println(sentence);
					builder.append(sentence).append("\n");
				}
				arr.add(builder.toString());
			}
		return arr.toArray(new String[arr.size()]);

	}
	
	@Override
	public String[] simpleTokenizer(String lineswithsplitter) {
		return opennlp.tools.tokenize.SimpleTokenizer.INSTANCE.tokenize(lineswithsplitter);
	}



	@Override
	public String[] tokenizerMEArr(String[] lines) {
		String nerModels = prop.getProperty(Tokenizer.PROP_COMPNENT_MODEL,
				"en-token.bin");
		logger.info("parser(): load " + nerModels + ".");

		ArrayList<TokenizerME> chunkers = new ArrayList<TokenizerME>();

		for (String nerModel : nerModels.split(":")) {
			logger.info("init(): load " + nerModel + " ...");
			if (nerModel.trim().length() > 0) {
				TokenizerModel model = new TokenizerModelLoader()
						.load(locate(nerModel));

				TokenizerME tokenizer = new TokenizerME(model);
				chunkers.add(tokenizer);

			}
		}

		ArrayList<String> arr = new ArrayList<String>();

		for (TokenizerME tokenizer : chunkers)
			for (String line : lines) {
				for (String token : tokenizer.tokenize(line)) {
					arr.add(token);
				}
			}
		return arr.toArray(new String[arr.size()]);
	}

	@Override
	public String[] tokenNameFinderArr(String[] lines) {

		String nerModels = prop.getProperty(
				NamedEntityRecognizer.PROP_COMPNENT_MODEL, "en-ner-person.bin");
		logger.info("parser(): load " + nerModels + ".");

		ArrayList<NameFinderME> chunkers = new ArrayList<NameFinderME>();

		for (String nerModel : nerModels.split(":")) {
			logger.info("init(): load " + nerModel + " ...");
			if (nerModel.trim().length() > 0) {
				TokenNameFinderModel model = new TokenNameFinderModelLoader()
						.load(locate(nerModel));
				chunkers.add(new NameFinderME(model));
			}
		}
		ArrayList<String> arr = new ArrayList<String>();

		for (String line : lines) {
			String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
					.tokenize(line);
			for (NameFinderME nameFinder : chunkers) {

				// A new line indicates a new document,
				// adaptive data must be cleared for a new document

				if (whitespaceTokenizerLine.length == 0) {
					nameFinder.clearAdaptiveData();
				}

				List<Span> names = new ArrayList<Span>();
				Collections.addAll(names,
						nameFinder.find(whitespaceTokenizerLine));

				// Simple way to drop intersecting spans, otherwise the
				// NameSample is invalid
				Span reducedNames[] = NameFinderME.dropOverlappingSpans(names
						.toArray(new Span[names.size()]));
				System.out.println(Arrays.toString(reducedNames));
				NameSample nameSample = new NameSample(whitespaceTokenizerLine,
						reducedNames, false);
				String namesamples = nameSample.toString();
				// System.out.println(namesamples);
				arr.add(namesamples);
			}
		}
		return arr.toArray(new String[arr.size()]);
	}

	@Override
	public String[] chunkerMET(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return chunkerMETArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
				SPLITTER_LINE));
	}

	@Override
	public String[] coreferencer(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return coreferencerArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
				SPLITTER_LINE));
	}

	@Override
	public String[] dictionaryDetokenizer(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return dictionaryDetokenizerArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
				SPLITTER_LINE));
	}

	@Override
	public String[] doccat(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return doccatArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
				SPLITTER_LINE));
	}

	@Override
	public String[] parser(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return parserArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
				SPLITTER_LINE));
	}

	@Override
	public String[] sentenceDetector(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return sentenceDetectorArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
				SPLITTER_LINE));
	}

	@Override
	public String[] simpleTokenizerArr(String[] lines)
			throws OpenNLPWebServiceException {
		ArrayList<String> arr = new ArrayList<String>(16);
		for (String line : lines) {
			for (String token : simpleTokenizer(line)) {
				arr.add(token);
			}
		}
		return arr.toArray(new String[arr.size()]);
	}

	@Override
	public String[] tokenizerME(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return tokenizerMEArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
				SPLITTER_LINE));
	}

	@Override
	public String[] tokenNameFinder(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return tokenNameFinderArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
				SPLITTER_LINE));
	}


}
