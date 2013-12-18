#                                                #
# @file:    lapps_common_io.py
# @author:    shicq@cs.brandeis.edu
# @brief:   base io functions
# @date:    dec 14, 2013
# @url:    http://docs.python.org/2/library/optparse.html
#                                                #
 
import sys, os, time, inspect, imp
import cPickle as pickle
import ConfigParser

from optparse import OptionParser

from lapps_common_io import runPythonSectionConf, runPythonSection, runPythonSectionConfArgs


CONS_USAGE = "usage: %prog [options]"


def main():        
    parser = OptionParser(CONS_USAGE)
    parser.set_defaults(verbose=True)
    parser.add_option("-s", "--section", action="store", type="string", nargs=1, dest="section",
                      help="read section name in lapps.conf")
    parser.add_option("-i", "--inputs", action="store", type="string", nargs=3, dest="inputs",
                      help="only inputs  (section, module, args)")
    parser.add_option("-a", "--all", action="store", type="string", nargs=7, dest="allargs",
                      help="all the configuration strings of (module, loads, globals, function, args, return, dumps)")
    (options, args) = parser.parse_args()
    if len(args) != 0:
        parser.error("incorrect number of arguments")
    elif not options.section == None:
        print runPythonSectionConf(options.section)
    elif not options.allargs == None and len(options.allargs) == 7:
        print runPythonSection(options.allargs[0],options.allargs[1],options.allargs[2],options.allargs[3],options.allargs[4],options.allargs[5],options.allargs[6])
    elif not options.inputs == None and len(options.inputs) == 3:
        print runPythonSectionConfArgs(options.inputs[0],options.inputs[1],options.inputs[2]);    
    elif options.verbose:    
        parser.error("incorrect number of options")
        
if __name__ == "__main__":
    main()

