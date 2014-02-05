package edu.brandeis.cs.python.examples.nltk;

import java.util.ArrayList;

import edu.brandeis.cs.python.utils.PythonRunner;
import edu.brandeis.cs.python.utils.PythonRunnerException;



/**
 * @brief Naive Bayes Classifier implementation using Python based nltk;
 * @author shicq@cs.brandeis.edu
 *
 */
public class NaiveBayesClassifier implements IClassify {
	private PythonRunner pr = null;
	
	public NaiveBayesClassifier() throws PythonRunnerException{
		pr = new PythonRunner();
	}
	
	public NaiveBayesClassifier(PythonRunner pr) throws PythonRunnerException{
		this.pr = pr;
	}
	@Override
	public String train(String featuresID) throws PythonRunnerException {
		pr.runPythonSection("edu.brandeis.cs.python.examples.nltk.NaiveBayesClassifier.train", featuresID);
		return featuresID;
	}

	@Override
	public String predict(String classifierID, String input)
			throws PythonRunnerException {
		String ret = pr.runPythonSection("edu.brandeis.cs.python.examples.nltk.NaiveBayesClassifier.predict", classifierID, input);
		return ret;
	}

	@Override
	public String[] listFeatureSets() throws PythonRunnerException {
		ArrayList arr = (ArrayList) pr.runPythonSectionPyro("edu.brandeis.cs.python.examples.nltk.NaiveBayesClassifier.listFeatureSets");
		return (String[])arr.toArray(new String[arr.size()]);
	}


}
