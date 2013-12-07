package edu.brandeis.cs.nltk.classifier;

import edu.brandeis.cs.nltk.IClassify;


/**
 * @brief Naive Bayes Classifier implementation using Python based nltk;
 * @author shicq@cs.brandeis.edu
 *
 */
public class NaiveBayesClassifier extends AbstractPythonClassifier implements IClassify {

	public static final String PY_CF_DECISION_TREE_TRAIN = "pythons/cf_naive_bayes_pos_tagger_train.py";
	public static final String PY_CF_DECISION_TREE_PREDICT = "pythons/cf_naive_bayes_pos_tagger_predict.py";
	public static final String FILEPREFIX = "naive_bayes_._";
	
	@Override
	public String getTrainPython() {
		return PY_CF_DECISION_TREE_TRAIN;
	}

	@Override
	public String getPredictPython() {
		return PY_CF_DECISION_TREE_PREDICT;
	}
	
	@Override
	protected String getClassifierFileNames(String classifierID) {
		return FILEPREFIX + classifierID;
	}

}
