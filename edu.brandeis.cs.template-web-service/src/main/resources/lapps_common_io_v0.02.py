#                                                #
# @file:    lapps_common_io.py
# @author:    shicq@cs.brandeis.edu
# @brief:   base io functions
# @date:    dec 18, 2013
#                                                #
 
import sys, os, time, inspect, imp, platform
import cPickle as pickle
import ConfigParser
import logging



if sys.version_info < (2, 6):
    import warnings
    warnings.warn("This lapps_common_io version is unsupported on Python versions older than 2.6", ImportWarning)
    
##  ##                  ##  ##
#        CONSTANTS           #               
##  ##                  ##  ##    

CON_VERSION = "0.02"

CON_SECTION_DEFAULT = "default"
CON_PICKLE_PATH = "PickleHome"

CON_PYTHON_SECTION_GLOBALS = "Globals"
CON_PYTHON_SECTION_LOADS = "Loads"
CON_PYTHON_SECTION_FILE = "PythonFile"
CON_PYTHON_SECTION_FUNC = "Method"
CON_PYTHON_SECTION_ARGS = "Args"
CON_PYTHON_SECTION_RETURN = "Return"
CON_PYTHON_SECTION_DUMPS = "Dumps"
CON_PYTHON_SECTION_REQUIRES = "Requires"

CONF_PYTHON_SECTION_REQUIRES_SEPARATOR = ","
CONF_PYTHON_SECTION_GLOBALS_SEPARATOR = ","
CONF_PYTHON_SECTION_GLOBALS_SETATTR_SEPARATOR = ":"
CONF_PYTHON_SECTION_LOADS_SEPARATOR = ","
CONF_PYTHON_SECTION_LOADS_PICKLE_SEPARATOR = ":"


DEFAULT_CONF_FILE = "lapps.conf"
DEFAULT_PICKLE_DIR = "pickle_directory"
DEFAULT_PICKLE_SUFFIX = ".pkl"


log = logging.getLogger("lapps_common_io")
logging.basicConfig(filename='lapps_common_io.log', filemode='w', level=logging.DEBUG)

class CommonIoError(Exception):
    """Generic base of all lapps_common_io specific errors."""
    pass
##
#   @brief: return Python file's directory
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
#    
#    @brief:    print $s as mylog.
##

# _is_debug = True
_is_debug = False
def mylog(s):
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
    file = os.path.join(path, pickleId)
    directory = os.path.dirname(file)
    if not os.path.exists(directory):
        os.makedirs(directory)   
    output = open(file, 'wb')
    pickle.dump(data, output)
    output.close()
    return pickleId
    
def pickleLoad(path, pickleId):
    file = os.path.join(path, pickleId)
    pkl_file = open(file, 'rb')
    data = pickle.load(pkl_file)
    pkl_file.close()
    return data


def split2array(conf, sep):
    if conf == None or conf.strip() == "":
        return []
    else:
        return conf.strip().split(sep)


class PicklePair(object):
    __slots = ("objName", "pickleId")
    def __init__(self, str, sep):
        if not sep in str:
            error = "illegal pickle pair string: %s not in %s" % sep, str
            raise CommonIoError(error)    
        pair = str.split(sep)
        self.objName = pair[0].strip()
        self.pickleId = pair[1].strip()
        
class GlobalPair(object):
    __slots = ("globalRef", "objName")
    def __init__(self, str, sep):
        if not sep in str:
            error = "illegal global variable pair string: %s not in %s" % sep, str
            raise CommonIoError(error)    
        pair = str.split(sep)
        self.globalRef = pair[0].strip()
        self.objName = pair[1].strip()
    
##
#    
#    @brief:    hold configuration of section
#    
##
class SectionConf(object):
    __slots__=("Requires", "Loads", "PythonFile", "Globals", "Method", "Args", "Return", "Dumps",
               "RequiresConf", "LoadsConf", "PythonFileConf", "GlobalsConf", "MethodConf", "ArgsConf", "ReturnConf", "DumpsConf",
               "SECTION_LOADS_PICKLE_SEPARATOR",
               "SECTION_ARGS_SEPARATOR",  
               "SECTION_REQUIRES_SEPARATOR",
               "SECTION_GLOBALS_SEPARATOR",
               "SECTION_GLOBALS_SETATTR_SEPARATOR",
               "SECTION_DUMPS_SEPARATOR",
               "SECTION_DUMPS_PICKLE_SEPARATOR",
               "SECTION_LOADS_SEPARATOR")
        
    def __init__(self):        
        ''' section items '''
        self.Requires = []
        self.Loads = []
        self.PythonFile = ""
        self.Globals = []
        self.Method = ""
        self.Args = []
        self.Return = ""
        self.Dumps = []
        ''' section item configuration '''
        self.RequiresConf = ""
        self.LoadsConf = ""
        self.PythonFileConf = ""
        self.GlobalsConf = ""
        self.MethodConf = ""
        self.ArgsConf = ""
        self.ReturnConf = ""
        self.DumpsConf = ""                
        ''' default separators '''
        self.SECTION_LOADS_PICKLE_SEPARATOR = ":"
        self.SECTION_LOADS_SEPARATOR = ","
        self.SECTION_DUMPS_PICKLE_SEPARATOR = self.SECTION_LOADS_PICKLE_SEPARATOR
        self.SECTION_DUMPS_SEPARATOR = self.SECTION_LOADS_SEPARATOR        
        self.SECTION_GLOBALS_SETATTR_SEPARATOR=  ":"
        self.SECTION_GLOBALS_SEPARATOR = ","
        self.SECTION_REQUIRES_SEPARATOR = ","
        self.SECTION_ARGS_SEPARATOR = ","
        
    def asDict(self):
        result={}
        for item in self.__slots__:
            result[item]=getattr(self,item)
        return result  
    
    def dump(self):
        result = []
#         for n, v in sorted(self.asDict()):
#             result.append("%s = %s" % (n, v))
        dict = self.asDict()
        for n in dict.iterkeys():
             result.append("%s = %s" % (n, dict.get(n)))
        return "\n".join(result)
    
    def splitRequires(self):
        self.Requires = split2array(self.RequiresConf, self.SECTION_REQUIRES_SEPARATOR) 
        return self.Requires
    
    def splitArgs(self):
        if not self.ArgsConf == None and not self.ArgsConf.strip() == "":
            self.ArgsConf = self.ArgsConf.strip()
        self.Args = split2array(self.ArgsConf, self.SECTION_ARGS_SEPARATOR) 
        return self.Args
    
    def splitGlobals(self):
        globalConfs = split2array(self.GlobalsConf, self.SECTION_GLOBALS_SEPARATOR)
        for globalConf in globalConfs:
            glob = GlobalPair(globalConf.strip())
            self.Globals.append(glob)
        return self.Globals
    
    def splitLoads(self):
        loadConfs = split2array(self.LoadsConf, self.SECTION_LOADS_SEPARATOR)
        for loadConf in loadConfs:
            load = PicklePair(loadConf.strip(), self.SECTION_LOADS_PICKLE_SEPARATOR)
            self.Loads.append(load)
        return self.Loads

    def splitDumps(self):
        dumpConfs = split2array(self.DumpsConf, self.SECTION_DUMPS_SEPARATOR)
        for dumpConf in dumpConfs:
            dump = PicklePair(dumpConf.strip(), self.SECTION_DUMPS_PICKLE_SEPARATOR)
            self.Dumps.append(dump)
        return self.Dumps
    
    def splitPythonFile(self):
        if not self.PythonFileConf == None and not self.PythonFileConf.strip() == "":
            pythonFile = self.PythonFileConf.strip()
            if not os.path.isabs(pythonFile):
                pythonFile = os.path.join(directory(), pythonFile)
            self.PythonFile = pythonFile

    def splitMethod(self):
        if not self.MethodConf == None and not self.MethodConf.strip() == "":
            self.Method = self.MethodConf.strip()

    def splitReturn(self):
        if not self.ReturnConf == None and not self.ReturnConf.strip() == "":
            self.Return = self.ReturnConf.strip()
            
    def split(self):
        self.splitRequires()
        self.splitArgs()
        self.splitLoads()
        self.splitGlobals()        
        self.splitPythonFile()
        self.splitMethod()
        self.splitReturn()
        self.splitDumps()



class CommonIOConf(object):
    ''' http://stackoverflow.com/questions/472000/python-slots '''
    __slots__=("CONF_FILE", "PICKLE_DIR", "PICKLE_SUFFIX", "LOGWIRE", "isConfLoaded", "config")
    
    def __init__(self):
        self.reset()
        
    def reset(self):
        self.CONF_FILE = DEFAULT_CONF_FILE
        self.PICKLE_DIR = DEFAULT_PICKLE_DIR
        self.PICKLE_SUFFIX = DEFAULT_PICKLE_SUFFIX
        self.LOGWIRE = True   # log wire-level messages
        self.isConfLoaded = False
        self.config = ConfigParser.ConfigParser()    
        self.config.optionxform=str
        
    def asDict(self):
        result={}
        for item in self.__slots__:
            result[item]=getattr(self,item)
        return result  
    
    def dump(self):
        config=self.asDict()   
        if hasattr(platform, "python_implementation"):
            implementation = platform.python_implementation()
        else:
            implementation = "Jython" if os.name=="java" else "???"
        result=["lapps_commmon_io version: %s" % CON_VERSION,
                "Loaded from: %s" % os.path.abspath(os.path.split(inspect.getfile(CommonIOConf))[0]),
                "Python version: %s %s (%s, %s)" % (implementation, platform.python_version(), platform.system(), os.name),
                "Active configuration settings:"]   
        for n, v in sorted(config.items()):
            result.append("%s = %s" % (n, v))
        return "\n".join(result)        
    
    def _loadConf(self):
        _config_file = self.CONF_FILE
        if not os.path.isabs(_config_file):
            if os.path.exists(os.path.join(directory(), self.CONF_FILE)):
                _config_file = os.path.join(directory(), self.CONF_FILE)
            if os.path.exists(os.path.join(currDir(), self.CONF_FILE)):
                _config_file = os.path.join(currDir(), self.CONF_FILE)
            if os.path.exists(os.path.join(home(), self.CONF_FILE)):
                _config_file = os.path.join(home(), self.CONF_FILE)
        if os.path.exists(_config_file):
            if self.LOGWIRE:                
                log.debug("CommonIOConf._loadConf: CONF_FILE=%s", _config_file)
            self.config.read(_config_file)
            self.CONF_FILE = _config_file
            self.isConfLoaded = True
        else:
            error = "config file not exist: %s" % self.CONF_FILE
            raise CommonIoError(error)    

    '''  @brief: read configuration file. '''
    def getConf(self, section, key):
        if not self.isConfLoaded:
            self._loadConf()
        try:   
            _value = self.config.get(section, key)
        except ConfigParser.NoSectionError:
            _value = None
        except ConfigParser.NoOptionError:  
            _value = None   
        return _value
    
    def reloadConf(self):
        self.isConfLoaded = False
    
    def setConf(self, section, key, value):
        if not self.isConfLoaded:
            self._loadConf()
        self.config.set(section, key, value)
        _config_file = self.CONF_FILE
        if not os.path.isabs(_config_file):
            _config_file = os.path.join(directory(), self.CONF_FILE)
            self.CONF_FILE = _config_file 
        with open(self.CONF_FILE, 'wb') as configfile:
            self.config.write(configfile)
        return value    
    '''   @brief: read default section '''            
    def getConfDef(self, key):
        return self.getConf(CON_SECTION_DEFAULT, key)
    def setConfDef(self, key, value):
        return self.setConf(CON_SECTION_DEFAULT, key, value)    
    '''' @brief: dump and load data from default path. '''
    def picklePath(self):
        _directory = self.getConfDef(CON_PICKLE_PATH)
        if not _directory == None and not _directory == "": 
            if not os.path.isabs(_directory):
                _directory = os.path.join(directory(), _directory)
        else:
            _directory = self.PICKLE_DIR
            if not os.path.isabs(_directory):
                _directory = os.path.join(directory(), self.PICKLE_DIR)
        self.PICKLE_DIR = _directory
#         self.setConfDef(CON_PICKLE_PATH, _directory)
        if self.LOGWIRE:                
            log.debug("pickleDefDump: directory=%s", _directory)
        return _directory
    
##
#   @brief: dynamically load module from Python file.
#
##
def loadModule(pyFile):    
    if os.path.exists(pyFile):
        _pyPath = os.path.dirname(os.path.expanduser(pyFile))
        sys.path.append(_pyPath)
        _mod_name, _file_ext = os.path.splitext(os.path.split(pyFile)[-1])
        _mod = imp.load_source(_mod_name, pyFile)
        return _mod
    else:
        error = "illegal Python file: %s" % pyFile
        raise CommonIoError(error)    


def loadModuleAs(pyFile, modName):
    if os.path.exists(pyFile):
        _pyPath = os.path.dirname(os.path.expanduser(pyFile))
        sys.path.append(_pyPath)
        _mod = imp.load_source(modName, pyFile)
        return _mod
    else:
        error = "illegal Python file: %s" % pyFile
        raise CommonIoError(error)    

##
#   @brief: dynamically run module's function.
#
##
def runModuleFunc(module, method, *args, **kwargs):
    _callable = getattr(module, method)
    if callable(_callable):
        return _callable(*args, **kwargs)

def runModuleFuncArgsLine(module, method, argsline):
    return eval("runModuleFunc(module, '" + method + "', " + argsline + ")")


config = CommonIOConf()

def useConfig(_config):
    config = _config

##
#   @brief: call a Python file and run one function.
#
##
def runPythonFunc(pyFile, method, *args, **kwargs):
    _module = loadModule(pyFile)
    return runModuleFunc(_module, method, *args, **kwargs)


def runPythonFuncArgsLine(pyFile, method, argsline):
    _module = loadModule(pyFile)
    return eval("runModuleFunc(_module, '" + method + "', " + argsline + ")")

def pickleDefDump(data, pickleId):
    _directory = config.picklePath()
    return pickleDump(data, _directory, pickleId + config.PICKLE_SUFFIX)    
def pickleDefLoad(self, pickleId):
    _directory = config.picklePath()        
    return pickleLoad(_directory, pickleId + config.PICKLE_SUFFIX)

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

def dumpFromLine(line, pickleId):
    if config.LOGWIRE :
        log.debug("dumpFromLine(): line=%s ; pickId=%s", line, pickleId)
    _namePickleObj = eval(line)
    pickleDefDump(_namePickleObj, pickleId)
    return _namePickleObj

class SectionRun(object):
    def __init__(self):
        self.sectionConf = SectionConf()
        self.globalModuel = sys.modules[__name__]
        self.localModule = None
        self.globalObjs = []
        self.localObjs = []
         
    def readSectionConf(self, section):
        self.sectionConf.RequiresConf = config.getConf(section, CON_PYTHON_SECTION_REQUIRES)
        self.sectionConf.ArgsConf = config.getConf(section, CON_PYTHON_SECTION_ARGS)
        self.sectionConf.GlobalsConf = config.getConf(section, CON_PYTHON_SECTION_GLOBALS)  
        self.sectionConf.PythonFileConf = config.getConf(section, CON_PYTHON_SECTION_FILE)
        self.sectionConf.LoadsConf = config.getConf(section, CON_PYTHON_SECTION_LOADS)
        self.sectionConf.MethodConf = config.getConf(section, CON_PYTHON_SECTION_FUNC)
        self.sectionConf.ReturnConf = config.getConf(section, CON_PYTHON_SECTION_RETURN)
        self.sectionConf.DumpsConf = config.getConf(section, CON_PYTHON_SECTION_DUMPS)
        if config.LOGWIRE :
            log.debug("SectionRun.readSectionConf(): sectionConf :: %s", self.sectionConf.dump())

    def loads(self):
        self.sectionConf.splitLoads()
        for load in self.sectionConf.Loads:
            obj = loadToAttr(self.globalModuel, load.objName, load.pickleId)
            self.globalObjs.append(obj)
            
    def module(self):
        self.sectionConf.splitPythonFile()
        if config.LOGWIRE :
            log.debug("SectionRun.module(): sectionConf.PythonFile=%s", self.sectionConf.PythonFile)
        if not self.sectionConf.PythonFile == "":
            self.localModule = loadModule(self.sectionConf.PythonFile)
    
    def globals(self):
        self.sectionConf.splitGlobals()
        for glob in self.sectionConf.Globals:
            obj = eval(glob.objName)
            if self.localModule == None:
                error = "local module has not been loaded."
                raise CommonIoError(error)
            setattr(self.localModule, glob.globalRef, obj)
            self.localObjs.append(glob.globalRef)     
    
    def func(self):
        self.sectionConf.splitMethod()
        if not self.sectionConf.Method == "":
            self.sectionConf.splitArgs()
            if self.localModule == None:
                error = "local module has not been loaded."
                raise CommonIoError(error)    
            ret = runModuleFuncArgsLine(self.localModule, self.sectionConf.Method, self.sectionConf.ArgsConf)
#            _returnConf = getConf(pySectionName, CON_PYTHON_SECTION_RETURN)
            if not ret == None:
                self.sectionConf.splitReturn()
                setattr(self.globalModuel, self.sectionConf.Return, ret)
                self.globalObjs.append(self.sectionConf.Return) 
                return ret
        return None
    
    def dumps(self):
        self.sectionConf.splitDumps()
        for dump in self.sectionConf.Dumps:
            dumpFromLine(dump.objName, dump.pickleId)
 
    def clear(self):
        for objName in self.localObjs:
            if self.localModule == None:
                error = "local module has not been loaded."
                raise CommonIoError(error)    
            delattr(self.localModule, objName)
        for gobjName in self.globalObjs:
            delattr(self.globalModuel, gobjName)   
        
    def run(self):
        reqRets = self.requires()
        self.loads()
        self.module()
        self.globals()
        ret = self.func()
        self.dumps()
        self.clear()
        if not ret == None:
            return ret
        if not reqRets == []:
            return reqRets
        return None
            
    def requires(self):
        self.sectionConf.splitRequires()
        rets = []
        for req in self.sectionConf.Requires:
            secRun = SectionRun()
            secRun.readSectionConf(req)
            ret = secRun.run()
            rets.append(ret)
        return rets

def runPythonSectionConf(pySectionName):
    secRun = SectionRun()
    secRun.readSectionConf(pySectionName)
    ret = secRun.run()
    return ret


def runPythonSectionConfArgs(pySectionName, moduleConf, argsConf):
    secRun = SectionRun()
    secRun.readSectionConf(pySectionName)
    secRun.sectionConf.PythonFileConf = moduleConf
    secRun.sectionConf.ArgsConf = argsConf
    ret = secRun.run()
    return ret

def runPythonSection(moduleConf, loadConf, globalConf, funcConf, argsConf, returnConf, dumpConf):
    sectionConf = SectionConf()
    sectionConf.PythonFileConf = moduleConf
    sectionConf.LoadsConf = loadConf
    sectionConf.GlobalsConf = globalConf
    sectionConf.MethodConf = funcConf
    sectionConf.ArgsConf = argsConf
    sectionConf.RequiresConf = returnConf
    sectionConf.DumpsConf = dumpConf
    secRun = SectionRun()
    secRun.sectionConf = sectionConf
    ret = secRun.run()
    return ret


def main():        
    print runPythonSectionConf(sys.argv[1])

if __name__ == "__main__":
    main()

