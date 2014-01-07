#                                                #
# @file:    test_lapps_common_io
# @author:    shicq@cs.brandeis.edu
# @brief:   base io functions
# @date:    dec 18, 2013
#                                                #
 
import sys, os, time, inspect, imp
import cPickle as pickle
import ConfigParser
import logging


import unittest

from lapps_common_io import CommonIOConf, SectionRun, useConfigFile

class TestCommonIOConf(unittest.TestCase):

    def setUp(self):
        self.conf = CommonIOConf()
        self.conf_file = open("TestCommonIOConf.ini", "w")
        self.conf.CONF_FILE = self.conf_file.name
        self.conf_file.write("[default]\n")
        self.conf_file.write("[helloworld.append]\n")
        self.conf_file.write("Loads=\n")
        self.conf_file.write("PythonFile=examples/helloworld.py\n")
        self.conf_file.write("Method=append\n")
        self.conf_file.write('Args="hello", "worlds"\n')
        self.conf_file.write("Return=example_hello_world\n")
        self.conf_file.write("Dumps=example_hello_world:helloworld.append.example_hello_world\n")
        self.conf_file.write("\n")
        self.conf_file.close()

    def tearDown(self):
        with open (self.conf_file.name, "r") as conf_file:
            data = conf_file.readlines()
#         print ""    
#         for line in data:
#             print line.replace("\n","")    
        os.remove(self.conf_file.name)
#         print self.conf.dump()

    def test_getConf(self):
        val = self.conf.getConf("helloworld.append", "Args")
        self.assertEqual(val, '"hello", "worlds"')
        val = self.conf.getConf("helloworld.append", "Loads")    
        self.assertEqual(val, "")
        val = self.conf.getConf("helloworld.append", "Argsss")    
        self.assertEqual(val, None)
        
    def test_setConf(self):
        self.conf.setConf("helloworld.append", "Argsss", "TEST")
        val = self.conf.getConf("helloworld.append", "Argsss")    
        self.assertEqual(val, "TEST")
        
    def test_picklePath(self):
        val = self.conf.picklePath()    
        self.assertTrue(os.path.isabs(val))
#         self.assertEqual(self.conf.getConfDef("PickleHome"), val)
        self.conf.setConfDef("PickleHome", "tmp")
        val = self.conf.picklePath()
        self.assertTrue(os.path.isabs(val))
        self.assertTrue("tmp" in val)
        self.conf.setConfDef("PickleHome", "")
        self.conf.PICKLE_DIR = "abs"
        val = self.conf.picklePath()
        self.assertTrue(os.path.isabs(val))
        self.assertTrue("abs" in val)

class TestSectionRun(unittest.TestCase):
    def setUp(self):
        self.conf_file = open("TestCommonIOConf.ini", "w")
        self.conf_file.write("[default]\n")
        self.conf_file.write("[helloworld.append]\n")
        self.conf_file.write("Loads=\n")
        self.conf_file.write("PythonFile=examples/helloworld.py\n")
        self.conf_file.write("Method=append\n")
        self.conf_file.write('Args="hello", "worlds"\n')
        self.conf_file.write("Return=example_hello_world\n")
        self.conf_file.write("Dumps=example_hello_world:helloworld.append.example_hello_world\n")
        self.conf_file.write("\n")
        self.conf_file.write("\n")
        self.conf_file.write("[helloworld.say]\n")
        self.conf_file.write("Loads=he:helloworld.dump.myhello\n")
        self.conf_file.write("Globals=hello:he\n")
        self.conf_file.write("PythonFile=examples/helloworld.py\n")
        self.conf_file.write("Method=say\n")
        self.conf_file.write("Args='world'\n")
        self.conf_file.write("Return=hello_world2\n")
        self.conf_file.write("Dumps=hello_world2:helloworld.say.hello_world2\n")
        self.conf_file.write("\n")
        self.conf_file.write("[helloworld.dump]\n")
        self.conf_file.write("Dumps='hi':helloworld.dump.myhello\n")
        self.conf_file.write("\n")
        self.conf_file.write("[helloworld]\n")
        self.conf_file.write("Requires=helloworld.dump, helloworld.say\n")
        self.conf_file.write("\n")        
        self.conf_file.close()
        useConfigFile(self.conf_file.name)
        self.sectionRun = SectionRun()     

    def tearDown(self):
        with open (self.conf_file.name, "r") as conf_file:
            data = conf_file.readlines()
#         print ""    
#         for line in data:
#             print line.replace("\n","")    
#         os.remove(self.conf_file.name)
#         print self.conf.dump()

    def test_runPythonSectionConf(self):
        secRun = self.sectionRun
        secRun.readSectionConf("helloworld.append")
        ret = secRun.run()
        self.assertEqual('hello worlds', ret)
        ''' test dump '''
        secRun.readSectionConf("helloworld")
        ret = secRun.run()
        self.assertEqual('None,hi world', ",".join(ret))
         

        
    def test_runPythonSectionConfArgs(self):
        secRun = self.sectionRun
        secRun.readSectionConf("helloworld.append")
        secRun.sectionConf.PythonFileConf = "examples/helloworld.py"
        secRun.sectionConf.ArgsConf = '"hi", "python"'
        ret = secRun.run()
        self.assertEqual("hi python", ret)
          

            
if __name__ == '__main__':
    unittest.main()

