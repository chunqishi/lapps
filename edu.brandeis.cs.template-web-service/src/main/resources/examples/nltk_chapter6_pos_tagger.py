'''
    /**
     * @brief predict classification label according to input using a trained model classifierID
     * @param input users's one input.
     * @param classifierID users set classifier. 
     * @return classification label.
     * @throws ClassifyException
     */
    public String predict(String input, String classifierID) throws ClassifyException;

'''

import sys, os, io, time
import nltk, cPickle as pickle

from nltk.corpus import brown

##
#    @brief:    constants
##
MAX_FEATURESET_LEN = 200

##
#    @brief:    predict needs trained classifier module, feature extractor, and input
##
def predict(classifier, input):
    return classifier.classify(pos_features(input))

def get_common_suffixes():
    suffix_fdist = nltk.FreqDist()
    for word in brown.words():
         word = word.lower()
         suffix_fdist.inc(word[-1:])
         suffix_fdist.inc(word[-2:])
         suffix_fdist.inc(word[-3:])
    common_suffixes = suffix_fdist.keys()[:100]
    return common_suffixes

common_suffixes = []

def pos_features(inputWord):
    features = {}    
    for suffix in common_suffixes:
        features['endswith(%s)' % suffix] = inputWord.lower().endswith(suffix)
    return features

def features_brown_news():
    tagged_words = brown.tagged_words(categories='news')
    featuresets = [(pos_features(n), g) for (n,g) in tagged_words]
    return featuresets


def trainDecisionTreeClassifier(featuresets):
    '''maximum MAX_FEATURESET_LEN(200)'''
    _max = min(MAX_FEATURESET_LEN, len(featuresets))
    _train_set = featuresets[1:_max]
    _classifier = nltk.DecisionTreeClassifier.train(_train_set)
    return _classifier

def trainNaiveBayesClassifier(featuresets):
    '''maximum MAX_FEATURESET_LEN(200)'''
    _max = min(MAX_FEATURESET_LEN, len(featuresets))
    _train_set = featuresets[1:_max]
    _classifier = nltk.NaiveBayesClassifier.train(_train_set)
    return _classifier

def listFeatureSets():
    return ["features_brown_news"]    