package edu.brandeis.cs.nltk.classifier;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.anc.resource.ResourceLoader;

import edu.brandeis.cs.nltk.ClassifyException;
import edu.brandeis.cs.nltk.IClassify;
import edu.brandeis.cs.python.PythonRunner;

public abstract class AbstractPythonClassifier extends PythonRunner implements IClassify {

	public final static String NULL_ID = "__NULL__";
	
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
	

	
	@Override
	public String train(String featuresID) throws ClassifyException {		
		String python = getResourcePath(getTrainPython());
		String classifierID = NULL_ID;
		try {
			classifierID = runPython(python, featuresID);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ClassifyException(e);
		}		
		return classifierID;
	}

	@Override
	public String predict(String input, String classifierID)
			throws ClassifyException {
		String python = getResourcePath(getPredictPython());
		String label = "";
		try {
			label = runPython(python, new String[] {input, classifierID});
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
		File directory =  getResourceFile("features/");
		ArrayList<String> arr = new ArrayList<String> (); 
		for (File file:directory.listFiles()){
			arr.add(file.getName());
		}
		return arr.toArray(new String[arr.size()]);
	}
	
//	public static void main(String []args){
//		String path =  getResourcePath("features/");
//		System.out.println(path);
//	}
}