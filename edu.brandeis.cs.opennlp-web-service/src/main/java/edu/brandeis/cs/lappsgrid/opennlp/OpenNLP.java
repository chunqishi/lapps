package edu.brandeis.cs.lappsgrid.opennlp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import edu.brandeis.cs.lappsgrid.api.opennlp.IVersion;
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
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import org.anc.resource.ResourceLoader;
import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.brandeis.cs.lappsgrid.api.opennlp.IOpenNLP;
import edu.brandeis.cs.lappsgrid.util.FileLoadUtil;
import edu.brandeis.cs.lappsgrid.util.SplitMergeUtil;

/**
 * <i>OpenNLP.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p>
 * <p>
 * <a href="http://opennlp.sourceforge.net/models-1.5/">Models for 1.5
 * series</a>
 * <p>
 * 
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>
 *         Nov 20, 2013<br>
 * 
 */
/**
 * <i>OpenNLP.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 21, 2013<br>
 * 
 */
/**
 * <i>OpenNLP.java</i> Language Application Grids (<b>LAPPS</b>)
 * <p> 
 * <p> 
 * <p> 
 *
 * @author Chunqi Shi ( <i>shicq@cs.brandeis.edu</i> )<br>Nov 21, 2013<br>
 * 
 */
public class OpenNLP implements IOpenNLP , IVersion {
	protected static final Logger logger = LoggerFactory
			.getLogger(OpenNLP.class);

	public OpenNLP() throws OpenNLPWebServiceException {
		init();
	}

	private Properties prop = new Properties();

	protected void init() throws OpenNLPWebServiceException {
		logger.info("init(): Creating OpenNLP ...");

		InputStream stream = ResourceLoader.open(FILE_PROPERTIES);
		if (stream == null) {
			logger.error("init(): fail to open \"" + FILE_PROPERTIES + "\".");
			throw new OpenNLPWebServiceException("init(): fail to open \""
					+ FILE_PROPERTIES + "\".");
		}

		try {
			prop.load(stream);
			stream.close();
		} catch (IOException e) {
			logger.error("init(): fail to load \"" + FILE_PROPERTIES + "\".");
			throw new OpenNLPWebServiceException("init(): fail to load \""
					+ FILE_PROPERTIES + "\".");
		}

		logger.info("init(): load " + FILE_PROPERTIES + ".");
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
		// if (tokenizer == null) {
		// try {
		// init();
		// } catch (OpenNLPWebServiceException e) {
		// logger.error("execute(): Fail to initialize Tokenizer");
		// return DataFactory
		// .error("execute(): Fail to initialize Tokenizer");
		// }
		// }
		//
		// String[] tokens = tokenize(data.getPayload());
		// logger.info("execute(): Execute OpenNLP tokenizer!");
		// return DataFactory.stringList(tokens);
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

	// one general loader.
	protected static abstract class Loader<T> {
		private ConcurrentHashMap<String, T> hashMap = new ConcurrentHashMap<String, T>(
				4);

		abstract T load(File file) throws OpenNLPWebServiceException;

		public ArrayList<T> loadAll(String models)
				throws OpenNLPWebServiceException {
			logger.info("loadAll(): load " + models + "...");
			ArrayList<T> arr = new ArrayList<T>();
			for (String nerModel : models.split(":")) {
				logger.info("loadAll(): load " + nerModel + " ...");
				T tool = null;
				if (nerModel.trim().length() > 0) {
					File file = FileLoadUtil.locate(nerModel);

					try {
						String key = file.getCanonicalPath();
						// PREVENT reload every time.
						if (FileLoadUtil.needUpdate(file)) {
							tool = load(file);
							hashMap.put(key, tool);
						} else {
							tool = hashMap.get(key);
//							System.out.println("loadAll(): cached " + key);
						}
					} catch (Exception e) {
						logger.error("loadAll(): fail to open MODEl \""
								+ nerModel + "\".");
						throw new OpenNLPWebServiceException(
								"loadAll(): fail to open MODEl \"" + nerModel
										+ "\".");
					}

					if (tool != null)
						arr.add(tool);
				}
			}
			return arr;
		}
	}



	
	
	private static Loader<Chunker> chunkerLoader = new Loader<Chunker>() {
		@Override
		Chunker load(File file) throws OpenNLPWebServiceException {
			ChunkerModel model = new ChunkerModelLoader()
			.load(file);
			Chunker chunker = new ChunkerME(model,
			ChunkerME.DEFAULT_BEAM_SIZE);
			return chunker;
		}
	};
	
	@Override
	public String[] chunkerMETArr(String[] lines)
			throws OpenNLPWebServiceException {
		// default English
		String model = prop.getProperty("Chunker", "en-chunker.bin");
		ArrayList<Chunker> meArr = chunkerLoader.loadAll(model);

		ArrayList<String> chunkArr = new ArrayList<String>();
		for (String line : lines) {
			POSSample posSample;
			try {
				posSample = POSSample.parse(line);
			} catch (InvalidFormatException e) {
				e.printStackTrace();
				logger.error("chunkerMETArr(): Invalid format:" + line);
				continue;
			}

			for (Chunker chunker : meArr) {
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
	
//	private static Loader<TreebankLinker> linkerLoader = new Loader<TreebankLinker>() {
//		@Override
//		TreebankLinker load(File file) throws OpenNLPWebServiceException {
//			TreebankLinker treebankLinker = null;
//			try {
//				treebankLinker = new TreebankLinker(file.toString(),
//						LinkerMode.TEST);
//			} catch (IOException e) {
//				logger.error("loadAll(): fail to open MODEl \"" + file + "\".");
//				throw new OpenNLPWebServiceException(
//						"loadAll(): fail to open MODEl \"" + file + "\".");
//			}
//			return treebankLinker;
//		}
//	};

	//FIXME: 
//	@Override
//	public String[] coreferencerArr(String[] lines) throws OpenNLPWebServiceException {
//
//		// default English
//		String models = prop.getProperty("Coreference", "coref");
//
//		ArrayList<TreebankLinker> linkArr = linkerLoader.loadAll(models);	
//        List<Mention> document = new ArrayList<Mention>();
//        List<Parse> parses = new ArrayList<Parse>();
//        
//		ArrayList<String> arr = new ArrayList<String>();
//        int sentenceNumber = 0;
//		for (TreebankLinker treebankLinker : linkArr) {
//			for (String line : lines) {
//				Parse p = Parse.parseParse(line);
//				parses.add(p);
//				Mention[] extents = treebankLinker.getMentionFinder()
//						.getMentions(new DefaultParse(p, sentenceNumber));
//				// construct new parses for mentions which don't have
//				// constituents.
//				for (int ei = 0, en = extents.length; ei < en; ei++) {
//					// System.err.println("PennTreebankLiner.main: "+ei+" "+extents[ei]);
//
//					if (extents[ei].getParse() == null) {
//						// not sure how to get head index, but its not used at
//						// this point.
//						Parse snp = new Parse(p.getText(),
//								extents[ei].getSpan(), "NML", 1.0, 0);
//						p.insert(snp);
//						extents[ei].setParse(new DefaultParse(snp,
//								sentenceNumber));
//					}
//
//				}
//				document.addAll(Arrays.asList(extents));
//				sentenceNumber++;
//			}
//			DiscourseEntity[] entities = treebankLinker.getEntities(document
//					.toArray(new Mention[document.size()]));
//			// showEntities(entities);
//			StringBuffer sb = new StringBuffer ();
//            //showEntities(entities);
//            new CorefParse(parses,entities).show(sb);
//            sentenceNumber=0;
//            document.clear();
//            parses.clear();
//            arr.add(sb.toString());
//		}
//        return arr.toArray(new String[arr.size()]);
//	}
	
	
//	private class CorefParse {
//		private Map<Parse, Integer> parseMap;
//		private List<Parse> parses;
//
//		public CorefParse(List<Parse> parses, DiscourseEntity[] entities) {
//			this.parses = parses;
//			parseMap = new HashMap<Parse, Integer>();
//			for (int ei = 0, en = entities.length; ei < en; ei++) {
//				if (entities[ei].getNumMentions() > 1) {
//					for (Iterator<MentionContext> mi = entities[ei]
//							.getMentions(); mi.hasNext();) {
//						MentionContext mc = mi.next();
//						Parse mentionParse = ((DefaultParse) mc.getParse())
//								.getParse();
//						parseMap.put(mentionParse, ei + 1);
//					}
//				}
//			}
//		}
//
//		public void show(StringBuffer sb) {
//			for (int pi = 0, pn = parses.size(); pi < pn; pi++) {
//				Parse p = parses.get(pi);
//				show(p, sb);
//				System.out.println();
//				sb.append("\n");
//			}
//		}
//
//		private void show(Parse p, StringBuffer sb) {
//			int start;
//			start = p.getSpan().getStart();
//			if (!p.getType().equals(opennlp.tools.parser.chunking.Parser.TOK_NODE)) {
//				System.out.print("(");
//				System.out.print(p.getType());
//				sb.append("(").append(p.getType());
//				if (parseMap.containsKey(p)) {
//					System.out.print("#" + parseMap.get(p));
//					sb.append("#" + parseMap.get(p));
//				}
//				// System.out.print(p.hashCode()+"-"+parseMap.containsKey(p));
//				System.out.print(" ");
//				sb.append(" ");
//			}
//			Parse[] children = p.getChildren();
//			for (int pi = 0, pn = children.length; pi < pn; pi++) {
//				Parse c = children[pi];
//				Span s = c.getSpan();
//				if (start < s.getStart()) {
//					System.out
//							.print(p.getText().substring(start, s.getStart()));
//					sb.append(p.getText().substring(start, s.getStart()));
//				}
//				show(c, sb);
//				start = s.getEnd();
//			}
//			System.out
//					.print(p.getText().substring(start, p.getSpan().getEnd()));
//			sb.append(p.getText().substring(start, p.getSpan().getEnd()));
//			if (!p.getType().equals(opennlp.tools.parser.chunking.Parser.TOK_NODE)) {
//				System.out.print(")");
//				sb.append(")");
//			}
//		}
//	}
		  
	
//	private static Loader<Detokenizer> detokenLoader = new Loader<Detokenizer>() {
//		@Override
//		Detokenizer load(File file) throws OpenNLPWebServiceException {
//			Detokenizer detokenizer = null;
//			FileInputStream in = null;
//			try {
//				try {
//					in = new FileInputStream(file);
//					detokenizer = new DictionaryDetokenizer(
//							new DetokenizationDictionary(in));
//				} finally {
//					in.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error("load(): fail to open MODEl \"" + file + "\".");
//				throw new OpenNLPWebServiceException(
//						"load(): fail to open MODEl \"" + file + "\".");
//			}
//			return detokenizer;
//		}
//	};
	
//	@Override
//	public String[] dictionaryDetokenizerArr(String[] lines)
//			throws OpenNLPWebServiceException {
//
//		String models = prop.getProperty("Chunker", "en-chunker.bin");
//
//		ArrayList<Detokenizer> chunkers = detokenLoader.loadAll(models);
//		ArrayList<String> arr = new ArrayList<String>();
//
//		
//		for (String tokenizedLine : lines)
//			for (Detokenizer detokenizer : chunkers) {
//				// white space tokenize line
//				String tokens[] = SimpleTokenizer.INSTANCE
//						.tokenize(tokenizedLine);
//
//				System.out.println(detokenizer.detokenize(tokens, null));
//
//				arr.add(detokenizer.detokenize(tokens, null));
//			}
//		return arr.toArray(new String[arr.size()]);
//	}

//	@Override
//	public String[] doccatArr(String[] lines) throws OpenNLPWebServiceException {
//		throw new OpenNLPWebServiceException(
//				"doccat(): to be implemented exception");
//	}


	private static Loader<opennlp.tools.parser.Parser> parserLoader = 
			new Loader<opennlp.tools.parser.Parser>() {
		@Override
		opennlp.tools.parser.Parser load(File file) {
			ParserModel model = new ParserModelLoader().load(file);
			opennlp.tools.parser.Parser parser = ParserFactory.create(model);
			return parser;
		}
	};

	@Override
	public String[] parserArr(String[] lines) throws OpenNLPWebServiceException {
		String nerModels = prop.getProperty(Parser.PROP_COMPNENT_MODEL,
				"en-parser-chunking.bin");
		ArrayList<opennlp.tools.parser.Parser> parserArr = parserLoader
				.loadAll(nerModels);

		ArrayList<String> arr = new ArrayList<String>();

		for (opennlp.tools.parser.Parser parser : parserArr)
			for (String tokenizedLine : lines) {
				StringBuffer builder = new StringBuffer();
				opennlp.tools.parser.Parse[] parses = ParserTool.parseLine(
						tokenizedLine, parser, 1);

				for (int pi = 0, pn = parses.length; pi < pn; pi++) {
					parses[pi].show(builder);
					builder.append("\n");
				}

				arr.add(builder.toString());
			}

		return arr.toArray(new String[arr.size()]);
	}
	
	
	
	private static Loader<SentenceDetector> sentDeteLoader = 
			new Loader<SentenceDetector>() {
		@Override
		SentenceDetector load(File file) throws OpenNLPWebServiceException {
			SentenceDetectorME sdetector = null;
			SentenceModel model;
			try {
				model = new SentenceModel(file);
				sdetector = new SentenceDetectorME(model);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("load(): fail to open MODEl \"" + file + "\".");
				throw new OpenNLPWebServiceException(
						"load(): fail to open MODEl \"" + file + "\".");
			}

			return sdetector;
		}
	};

	@Override
	public String[] sentenceDetectorArr(String[] lines)
			throws OpenNLPWebServiceException {
		String models = prop.getProperty(Splitter.PROP_COMPNENT_MODEL,
				"en-sent.bin");
		ArrayList<SentenceDetector> chunkers = sentDeteLoader.loadAll(models);

		ArrayList<String> arr = new ArrayList<String>();
//		StringBuffer builder = new StringBuffer();
		for (SentenceDetector sdetector : chunkers)
			for (String tokenizedLine : lines) {
				String[] sents = sdetector.sentDetect(tokenizedLine);
//				builder.setLength(0);
				for (String sentence : sents) {
					System.out.println(sentence);
//					builder.append(sentence).append("\n");
					arr.add(sentence);
				}
//				arr.add(builder.toString());
			}
		return arr.toArray(new String[arr.size()]);

	}

	@Override
	public String[] simpleTokenizer(String lineswithsplitter) {
		return opennlp.tools.tokenize.SimpleTokenizer.INSTANCE
				.tokenize(lineswithsplitter);
	}
	
	
	private static Loader<opennlp.tools.tokenize.Tokenizer> tokenizerLoader = 
			new Loader<opennlp.tools.tokenize.Tokenizer>() {
		@Override
		opennlp.tools.tokenize.Tokenizer load(File file) {
			TokenizerModel model = new TokenizerModelLoader().load(file);
			TokenizerME tokenizer = new TokenizerME(model);
			return tokenizer;
		}
	};

	@Override
	public String[] tokenizerMEArr(String[] lines) throws OpenNLPWebServiceException {
		String models = prop.getProperty(Tokenizer.PROP_COMPNENT_MODEL,
				"en-token.bin");
		ArrayList<opennlp.tools.tokenize.Tokenizer> meArr = tokenizerLoader.loadAll(models);

		ArrayList<String> arr = new ArrayList<String>();
		for (opennlp.tools.tokenize.Tokenizer tokenizer : meArr)
			for (String line : lines) {
				for (String token : tokenizer.tokenize(line)) {
					arr.add(token);
				}
			}
		return arr.toArray(new String[arr.size()]);
	}
	
	
	private static Loader<TokenNameFinder> nameFinderLoader = new Loader<TokenNameFinder>() {
		@Override
		TokenNameFinder load(File file) throws OpenNLPWebServiceException {
			TokenNameFinderModel model = new TokenNameFinderModelLoader()
					.load(file);
			return new NameFinderME(model);
		}
	};


	@Override
	public String[] tokenNameFinderArr(String[] lines) throws OpenNLPWebServiceException {

		String models = prop.getProperty(
				NamedEntityRecognizer.PROP_COMPNENT_MODEL, "en-ner-person.bin");
		ArrayList<TokenNameFinder> meArr = nameFinderLoader.loadAll(models);

		String[] arr = new String[lines.length];
		List<Span> names = new ArrayList<Span>();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			String tokens[] = SimpleTokenizer.INSTANCE.tokenize(line);

			for (TokenNameFinder nameFinder : meArr) {
				if (tokens.length == 0) {
					nameFinder.clearAdaptiveData();
				}

				Collections.addAll(names, nameFinder.find(tokens));
			}
			// Simple way to drop intersecting spans, otherwise the
			// NameSample is invalid
			Span reducedNames[] = NameFinderME.dropOverlappingSpans(names
					.toArray(new Span[names.size()]));

			NameSample nameSample = new NameSample(tokens, reducedNames, false);
			arr[i] = nameSample.toString();
		}
		return arr;
	}

	@Override
	public String[] chunkerMET(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return chunkerMETArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
				SPLITTER_LINE));
	}

//	@Override
//	public String[] coreferencer(String lineswithsplitter)
//			throws OpenNLPWebServiceException {
//		return coreferencerArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
//				SPLITTER_LINE));
//	}

//	@Override
//	public String[] dictionaryDetokenizer(String lineswithsplitter)
//			throws OpenNLPWebServiceException {
//		return dictionaryDetokenizerArr(SplitMergeUtil.fromStrToArr(
//				lineswithsplitter, SPLITTER_LINE));
//	}

//	@Override
//	public String[] doccat(String lineswithsplitter)
//			throws OpenNLPWebServiceException {
//		return doccatArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
//				SPLITTER_LINE));
//	}

	@Override
	public String[] parser(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return parserArr(SplitMergeUtil.fromStrToArr(lineswithsplitter,
				SPLITTER_LINE));
	}

	@Override
	public String[] sentenceDetector(String lineswithsplitter)
			throws OpenNLPWebServiceException {
		return sentenceDetectorArr(SplitMergeUtil.fromStrToArr(
				lineswithsplitter, SPLITTER_LINE));
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
		return tokenNameFinderArr(SplitMergeUtil.fromStrToArr(
				lineswithsplitter, SPLITTER_LINE));
	}

}
