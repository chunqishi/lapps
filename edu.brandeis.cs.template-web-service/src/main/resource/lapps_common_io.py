#                                                #
# @file:    lapps_common_io.py
# @author:    shicq@cs.brandeis.edu
# @brief:   base io functions
# @date:    dec 14, 2013
#                                                #
 
import sys, os, time, inspect, imp, shlex
import cPickle as pickle
import ConfigParser

##  ##                  ##  ##
#        CONSTANTS           #               
##  ##                  ##  ##    

CONF_FILE="lapps.conf"
CONF_SECTION_DEFAULT="default"
CONF_PICKLE_PATH="PickleHome"

CONF_PYTHON_SECTION_LOADS="Loads"
CONF_PYTHON_SECTION_LOADS_SEPARATOR=","
CONF_PYTHON_SECTION_LOADS_PICKLE_SEPARATOR=":"
CONF_PYTHON_SECTION_FILE="PythonFile"
CONF_PYTHON_SECTION_FUNC="Method"
CONF_PYTHON_SECTION_ARGS="Args"
CONF_PYTHON_SECTION_RETURN="Return"
CONF_PYTHON_SECTION_DUMPS="Dumps"

DEFAULT_PICKLE_DIR="pickle_directory"
DEFAULT_PICKLE_SUFFIX=".pkl"

##
#    
#    @brief:    print $s as log.
##

# _is_debug = True
_is_debug = False
def log(s):
    if (_is_debug == True):
        print time.strftime("%x") + " "+ time.strftime("%X") + " " + s

##
#    
#    @brief:    include other python file $filename
#    @exmpl:    include('code_suffix_pos_tag.py')
##
def include(filename):
    if os.path.exists(filename): 
        execfile(filename)

        
def getFileNames(directory, suffix):
    filenames = []
    for root, directories, files in os.walk(directory):
        for filename in files:
            if filename.endswith(suffix):
                filenames.append(filename)
    return filenames

##
#    
#    @brief:    use the pickleDump() and pickleLoad() functions. 
#               Note that a self-referencing list is pickled 
#               and restored correctly
##

def pickleDump(data, path, pickleId):         
    file = os.path.join(path, pickleId + DEFAULT_PICKLE_SUFFIX)
    directory = os.path.dirname(file)
    if not os.path.exists(directory):
        os.makedirs(directory)   
    output = open(file, 'wb')
    pickle.dump(data, output)
    output.close()
    return pickleId
    
def pickleLoad(path, pickleId):
    file = os.path.join(path, pickleId + DEFAULT_PICKLE_SUFFIX)
    pkl_file = open(file, 'rb')
    data = pickle.load(pkl_file)
    return data

##
#   @brief: return python file's directory
##
def directory():    
    py_file = inspect.getfile(inspect.currentframe())
    py_path = os.path.realpath(py_file)
    return os.path.dirname(py_path)

def currDir():
    return os.path.realpath(os.getcwd())

def home():    
    return os.path.dirname(directory()) 


##
#   @brief: read configuration file.
#
##
_config = ConfigParser.ConfigParser()

def getConf(section, key):
    _config_file = os.path.join(directory(), CONF_FILE)
    if os.path.exists(_config_file):
        _config.read(_config_file)
    return _config.get(section, key)

def setConf(section, key, value):
    _config.set(section, key, value)
    _config_file = os.path.join(directory(), CONF_FILE)
    with open(_config_file, 'wb') as configfile:
        _config.write(configfile)
        
def setConfDef(key, value):
    setConf(CONF_SECTION_DEFAULT, key, value)

##
#   @brief: read default section
#
##
def getConfDef(key):
    return getConf(CONF_SECTION_DEFAULT, key)


def picklePath():
    _directory = getConfDef(CONF_PICKLE_PATH)
    if not os.path.exists(_directory):
        _directory = os.path.join(currDir(), DEFAULT_PICKLE_DIR)
    return _directory

##
#   @brief: dump and load data from default path.
#
##
def pickleDefDump(data, pickleId):
    _directory = picklePath()
#DEBUG:    print _directory
    return pickleDump(data, _directory, pickleId)

def pickleDefLoad(pickleId):
    _directory = picklePath()        
    return pickleLoad(_directory, pickleId)


##
#   @brief: dynamically load module from Python file.
#
##
def loadModule(pyFile):
    _pyPath = os.path.dirname(os.path.expanduser(pyFile))
    sys.path.append(_pyPath)
    _mod_name, _file_ext = os.path.splitext(os.path.split(pyFile)[-1])
    _mod = imp.load_source(_mod_name, pyFile)
    return _mod

def loadModuleAs(pyFile, modName):
    _pyPath = os.path.dirname(os.path.expanduser(pyFile))
    sys.path.append(_pyPath)
    _mod = imp.load_source(modName, pyFile)
    return _mod

##
#   @brief: dynamically run module's function.
#
##
def runModuleFunc(module, method, *args, **kwargs):
    _callable = getattr(module, method)
    if callable(_callable):
        return _callable(*args, **kwargs)

def runModuleFuncArgsLine(module, method, argsline):
#DEBUG:    print "runModuleFunc(module, '" + method + "', " + argsline + ")"
    return eval("runModuleFunc(module, '" + method + "', " + argsline + ")")

##
#   @brief: call a Python file and run one function.
#
##
def runPythonFunc(pyFile, method, *args, **kwargs):
    _module = loadModule(pyFile)
    return runModuleFunc(_module, method, *args, **kwargs)


def runPythonFuncArgsLine(pyFile, method, argsline):
    _module = loadModule(pyFile)
#DEBUG:    print "runModuleFunc(_module, " + method + ", " + argsline+")"
    return eval("runModuleFunc(_module, '" + method + "', " + argsline + ")")


##
#   @brief: dynamically load pickle object and set as an attribute of the module.
#
##
def loadToAttr(module, attrName, pickleId):
    _namePickleObj = pickleDefLoad(pickleId)
    setattr(module, attrName, _namePickleObj)
    return _namePickleObj

def dumpFromAttr(module, attrName, pickleId):
    _namePickleObj = getattr(module, attrName)
    pickleDefDump(_namePickleObj, pickleId)
    return _namePickleObj

def runPythonConfSection(pySectionName):
    _moduleConf = getConf(pySectionName, CONF_PYTHON_SECTION_FILE)
    _module = loadModuleAs(_moduleConf, pySectionName)
    _loadConf = getConf(pySectionName, CONF_PYTHON_SECTION_LOADS)
    if CONF_PYTHON_SECTION_LOADS_SEPARATOR in _loadConf:
        _loads = _loadConf.split(CONF_PYTHON_SECTION_LOADS_SEPARATOR)
        for _load in _loads:
            if CONF_PYTHON_SECTION_LOADS_PICKLE_SEPARATOR in _load:
                _picklePair = _load.split(CONF_PYTHON_SECTION_LOADS_PICKLE_SEPARATOR)
                _objName = _picklePair[0].strip()
                _pickleId = _picklePair[1].strip()
                _globalModule = sys.modules[__name__]
                loadToAttr(_globalModule, _objName, _pickleId)
    _funcConf = getConf(pySectionName, CONF_PYTHON_SECTION_FUNC)
    _argsConf = getConf(pySectionName, CONF_PYTHON_SECTION_ARGS)
    _return = runModuleFuncArgsLine(_module, _funcConf, _argsConf)
    _returnConf = getConf(pySectionName, CONF_PYTHON_SECTION_RETURN)
    setattr(_module, _returnConf, _return)
    _dumpConf = getConf(pySectionName, CONF_PYTHON_SECTION_DUMPS)
    if CONF_PYTHON_SECTION_LOADS_PICKLE_SEPARATOR in _dumpConf:
        _picklePair = _dumpConf.split(CONF_PYTHON_SECTION_LOADS_PICKLE_SEPARATOR)
        _objName = _picklePair[0].strip()
        _pickleId = _picklePair[1].strip()
        dumpFromAttr(_module, _objName, _pickleId)
    return _return


