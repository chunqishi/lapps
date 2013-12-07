package edu.brandeis.cs.nltk.classifier;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.anc.resource.ResourceLoader;

import edu.brandeis.cs.nltk.ClassifyException;
import edu.brandeis.cs.nltk.IClassify;
import edu.brandeis.cs.python.PythonRunner;

public abstract class AbstractPythonClassifier extends PythonRunner implements IClassify {

	public final static String NULL_ID = "__NULL__";
	public final static String DIR_FEATURES = "features/";
	public final static String DIR_CLASSIFIERS = "classifiers/";
	
	/**
	 * 
	 * @return  Python client for training.
	 */
	public abstract String getTrainPython();
	
	/**
	 * 
	 * @return python client for predict.
	 */
	public abstract String getPredictPython();
	

	protected abstract String getClassifierFileNames(String classifierID);
	
	@Override
	public String train(String featuresID) throws ClassifyException {		
		String python = getResourcePath(getTrainPython());
		String classifierID = NULL_ID;
		
		String[] features = listFeatureSets();
		if (!Arrays.asList(features).contains(featuresID)) {
			throw new ClassifyException("train():" + featuresID +" is not in " + Arrays.toString(features));
		}
		
		
		try {			
			classifierID = runPython(python, featuresID);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ClassifyException("train(): fail " + python + " " +featuresID, e);
		}		
		return classifierID;
	}

	@Override
	public String predict(String classifierID, String input)
			throws ClassifyException {
		String python = getResourcePath(getPredictPython());
		
		String[] classifiers = listDirSets(DIR_CLASSIFIERS);
		
		if (!Arrays.asList(classifiers).contains(
				getClassifierFileNames(classifierID))) {
			throw new ClassifyException("predict():" + classifierID
					+ " is not in " + Arrays.toString(classifiers));
		}
		
		String label = "";
		try {
			label = runPython(python, new String[] {classifierID,input});
		} catch (IOException e) {
			e.printStackTrace();
			throw new ClassifyException(e);
		}		
		return label;
	}
	
	public static File getResourceFile(String fileName) {
		File resFile;
		try {
			resFile = new File(getClassLoader().getResource(fileName).toURI());
		} catch (Exception e) {
			resFile = new File(fileName);
			e.printStackTrace();
		}
		return resFile;
	}
	
	public static String getResourcePath(String fileName) {
		return getResourceFile(fileName).getAbsolutePath();
	}
	
	private static final ClassLoader getClassLoader() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = ResourceLoader.class.getClassLoader();
		}
		return loader;
	}

	@Override
	public String[] listFeatureSets() throws ClassifyException {
		try {
			File directory =  getResourceFile(DIR_FEATURES);
			ArrayList<String> arr = new ArrayList<String> (); 
			for (File file:directory.listFiles()){
				arr.add(file.getName());
			}
			return arr.toArray(new String[arr.size()]);
		}catch(Exception e){
			throw new ClassifyException("listFeatureSets(): Directory features/ does not exist.", e);
		}
	}
	
	
	public static String[] listDirSets(String dir) throws ClassifyException {
		try {
			File directory =  getResourceFile(dir);
			ArrayList<String> arr = new ArrayList<String> (); 
			for (File file:directory.listFiles()){
				arr.add(file.getName());
			}
			return arr.toArray(new String[arr.size()]);
		}catch(Exception e){
			throw new ClassifyException("listDirSets(): Directory \"" + dir
					+ "\" does not exist.", e);
		}
	}
	
	
	
	public static void main(String []args) throws IOException{
		String path =  getResourcePath("pythons/cf_decision_tree_pos_tagger_train.py");
		runPython("/home/j/llc/shicq/Project/chunqishi/lapps/edu.brandeis.cs.nltk-web-service/target/classes/pythons/cf_decision_tree_pos_tagger_train.py", "features_brown_news");
		System.out.println(path);
	}
}