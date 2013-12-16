#                                                #
# @file:    intfc_common_io.py
# @author:    shicq@cs.brandeis.edu
# @brief:   collect features and dump
#                                                #
 
import sys,os,time,nltk, cPickle as pickle

##
#    
#    @brief:    print $s as log.
##

# def log(s):
#     print time.strftime("%x") + " "+ time.strftime("%X") + " " + s

def log(s):
    pass

##
#    
#    @brief:    include other python file $filename
#    @exmpl:    include('code_suffix_pos_tag.py')
##
def include(filename):
    if os.path.exists(filename): 
        execfile(filename)

##
#    
#    @brief:    use the dump() and load() functions. Note that a self-referencing list is pickled and restored correctly
##

def dump(data, path, id):
    if not os.path.exists(path):
        os.makedirs(path)            
    file = os.path.join(path, id)
    output = open(file, 'wb')
    pickle.dump(data, output)
    output.close()
    
def load(path, id):
    file = os.path.join(path, id)
    pkl_file = open(file, 'rb')
    data = pickle.load(pkl_file)
    return data

def save_commons(commons, home,id):
    path = os.path.join(home, 'commons')
    dump(commons, path, id)
    
def load_commons(home,id):
    path = os.path.join(home, 'commons')
    commons = load(path, id)
    return commons

def save_features(features, home,id):
    path = os.path.join(home, 'features')
    dump(features, path, id)
    
def load_features(home,id):
    path = os.path.join(home, 'features')
    features = load(path, id)
    return features

def save_classifiers(classifiers, home,id):
    path = os.path.join(home, 'classifiers')
    dump(classifiers, path, id)
    
def load_classifiers(home,id):
    path = os.path.join(home, 'classifiers')
    classifiers = load(path, id)
    return classifiers

def home():    
    return os.path.dirname(os.path.dirname(os.path.realpath(sys.argv[0]))) 


