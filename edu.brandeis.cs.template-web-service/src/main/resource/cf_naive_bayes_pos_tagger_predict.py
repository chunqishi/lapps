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

from intfc_common_io import log,home,load_commons, load_classifiers
from intfc_nltk_features import pos_features

def main():
    classifierID = 'naive_bayes_._'+sys.argv[1]
    input = sys.argv[2]    
    path = home()    
    classifier = load_classifiers(path, classifierID)
    common_suffixes = load_commons(path, 'common_suffixes')
    print classifier.classify(pos_features(common_suffixes, input))

if __name__ == "__main__":
    main()

