#                                                #
# @file:    intfc_nltk_features.py
# @author:    shicq@cs.brandeis.edu
# @brief:   collect features and dump
#                                                #
 
import sys,os,time,nltk, cPickle as pickle
from intfc_common_io import log, home, save_features, load_features

from nltk.corpus import *
log("import nltk.corpus")


##
#    
#    @brief:    finding out what the most common suffixes
##

def get_common_suffixes():
    suffix_fdist = nltk.FreqDist()
    for word in brown.words():
         word = word.lower()
         suffix_fdist.inc(word[-1:])
         suffix_fdist.inc(word[-2:])
         suffix_fdist.inc(word[-3:])
     
    common_suffixes = suffix_fdist.keys()[:100]
    log("common_suffixes")    
    return common_suffixes


##
#    
#    @brief:    define a feature extractor function which checks a given word for these suffixes
##
def pos_features(common_suffixes, word):
    features = {}    
    for suffix in common_suffixes:
        features['endswith(%s)' % suffix] = word.lower().endswith(suffix)
    log("pos_features")
    return features

##
#    
#    @brief:    features brown corpus news categories into path directory.
#
#    @exmpl:    save_features(featuresets, '/home/j/llc/shicq/Project/chunqishi/lapps/edu.brandeis.cs.nltk-web-service/src/main/resources/', 'featuresets')
#               featuresets = load_features('/home/j/llc/shicq/Project/chunqishi/lapps/edu.brandeis.cs.nltk-web-service/src/main/resources/', 'featuresets')
#    log("load featuresets")
#    
##

def features_brown_news(path):
    name = 'features_brown_news'
    tagged_words = brown.tagged_words(categories='news')
    common_suffixes = get_common_suffixes()
    featuresets = [(pos_features(common_suffixes, n), g) for (n,g) in tagged_words]
    log("featuresets")
    save_features(featuresets, path, name)
    log("dump featuresets")
    return name

def main():
        features_brown_news(home())

if __name__ == "__main__":
    main()

