#                                                #
# @file:    intfc_post_tagger.py
# @author:    shicq@cs.brandeis.edu
# @brief:   use <a href="http://pythonhosted.org/Pyro4/intro.html" target="_blank">Pyro4</a> to register Python object.
#     

import sys,os,time, nltk, cPickle as pickle
import Pyro4, UserDict

ud = UserDict.UserDict({1:'b'})

daemon=Pyro4.Daemon()                 # make a Pyro daemon
uri=daemon.register(ud)   # register the greeting object as a Pyro object
print "Ready. Object uri =", uri      # print the uri so we can use it in the client later
daemon.requestLoop()                  # start the event loop of the server to wait for calls
