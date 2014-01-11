package edu.brandeis.cs.python.examples.nltk;

import jp.go.nict.langrid.commons.rpc.intf.Service;
import edu.brandeis.cs.python.utils.PythonRunnerException;

/**
 * @brief According to <a href="http://nltk.org/book/ch06.html" target="_blank">NLTK Chapter 6</a>
 *        Two main interface <b>train</b> and <b>predict</b> are defined. Since features are always extracted from input. 
 *        Thus, feature extractor is needed for further implementation.
 * @author shicq@cs.brandeis.edu
 *
 */

@Service(namespace="servicegrid:servicetype:brandeis:NLTKClassifier")
public interface IClassify {
	
	/**
	 * @brief train a classifier based on features. 
	 * @refer <a href="http://nltk.org/book/ch06.html" target="_blank">Supervised Classification</a>
	 * @return classifierID
	 * @throws ClassifyException
	 */
	public String train(String featuresID) throws PythonRunnerException;
	
	/**
	 * @brief predict classification label according to input using a trained model classifierID
	 * @param input users's one input.
	 * @param classifierID users set classifier. 
	 * @return classification label.
	 * @throws ClassifyException
	 */
	public String predict(String classifierID, String input) throws PythonRunnerException;
	
	/**
	 * 
	 * @return existing feature sets.
	 * @throws ClassifyException
	 */
	public String [] listFeatureSets() throws PythonRunnerException;
}



///**
// * @brief user setFeatureExtractor
// * @param featureExtractorID one feature extractor id. 
// * @throws ClassifyException
// */
//public void setFeatureExtractor(String featureExtractorID) throws ClassifyException;
//
///**
// * @brief list available feature extractors. 
// * @throws ClassifyException
// */
//public void getFeatureExtractors() throws ClassifyException;
