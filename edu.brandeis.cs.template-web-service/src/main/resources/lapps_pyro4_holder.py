##
#	@file:  lapps_pyro4_holder.py
#   @brief: provide memory holder using Pyro4
#	@url:	http://stackoverflow.com/questions/8508112/requestlooploopcondition-doesnt-release-even-after-loopcondition-is-false
#	@url:   http://pythonhosted.org/Pyro4/intro.html
#	@url:   http://pythonhosted.org/Pyro4/config.html
##

import Pyro4

DEFAULT_HOLDER_NAME = "PYRONAME:Holder"

Pyro4.config.COMPRESSION = True
Pyro4.config.SERIALIZER = 'pickle'
Pyro4.config.SERIALIZERS_ACCEPTED.add('pickle')

class Holder(object):
    def __init__(self):        self.data = {}
   
    def put(self, key, value):
        self.data[key] = value
        print "key="+key
        return value 
        
    def get(self, key):
        print "key="+key
        return self.data[key]
        
    @staticmethod
    def nameSpace():    	return DEFAULT_HOLDER_NAME
    
    def len(self):
    	return len(self.data)
		
def start():
    holder = Holder()
    Pyro4.Daemon.serveSimple( { holder: holder.nameSpace() }, ns = True)

def getHolder():
    holder = Pyro4.Proxy(Holder.nameSpace())
    return holder
	
def main():        
    start()

if __name__ == "__main__":
    main()
		
