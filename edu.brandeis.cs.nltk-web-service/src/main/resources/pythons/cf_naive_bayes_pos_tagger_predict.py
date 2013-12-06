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

import sys,os,ConfigParser,io,time,nltk, cPickle as pickle
from nltk.corpus import *

from intfc_common_io import log, load_commons, load_classifiers
from intfc_nltk_features import pos_features

input = sys.argv[1]
classifierID = 'naive_bayes_._'+sys.argv[2]

classifier = load_classifiers('..', classifierID)
common_suffixes = load_commons('..', 'common_suffixes')
print classifier.classify(pos_features(common_suffixes, input))
