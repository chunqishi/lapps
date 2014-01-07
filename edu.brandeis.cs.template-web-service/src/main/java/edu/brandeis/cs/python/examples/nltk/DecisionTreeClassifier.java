package edu.brandeis.cs.python.examples.nltk;

import edu.brandeis.cs.python.utils.PythonRunner;
import edu.brandeis.cs.python.utils.PythonRunnerException;



/**
 * @brief Decision Tree Classifier implementation using Python based nltk;
 * @author shicq@cs.brandeis.edu
 *
 */
public class DecisionTreeClassifier implements IClassify {
	private PythonRunner pr = null;
	
	public DecisionTreeClassifier() throws PythonRunnerException{
		pr = new PythonRunner();
	}
	
	public DecisionTreeClassifier(PythonRunner pr) throws PythonRunnerException{
		this.pr = pr;
	}
	@Override
	public String train(String featuresID) throws PythonRunnerException {
		String ret = pr.runPythonSection("edu.brandeis.cs.python.examples.nltk.DecisionTreeClassifier.train", featuresID);
		return ret;
	}

	@Override
	public String predict(String classifierID, String input)
			throws PythonRunnerException {
		String ret = pr.runPythonSection("edu.brandeis.cs.python.examples.nltk.DecisionTreeClassifier.predict", classifierID, input);
		return ret;
	}

//	@Override
//	public String[] listFeatureSets() throws PythonRunnerException {
//		String ret = pr.runPythonSection("edu.brandeis.cs.python.examples.nltk.DecisionTreeClassifier.listFeatureSets");
//		return ret;
//	}

	


}

