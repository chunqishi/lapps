##
#	@file:  lapps_pyro4_holder.py
#   @brief: provide memory holder using Pyro4
#	@url:	http://stackoverflow.com/questions/8508112/requestlooploopcondition-doesnt-release-even-after-loopcondition-is-false
#	@url:   http://pythonhosted.org/Pyro4/intro.html
#	@url:   http://pythonhosted.org/Pyro4/config.html
##

import Pyro4

DEFAULT_HOLDER_NAME = "Holder"

Pyro4.config.COMPRESSION = True

class Holder(object):
    def __init__(self):        self.data = {}
    	self.running = False
    	self.daemon = Pyro4.Daemon()
    	self.uri = self.daemon.register(self)  
   
    def put(self, key, value):
        self.data[key] = value
        return value 
        
    def get(self, key):
        return self.data[key]
        
    def getUri(self):
        return self.uri
        
    @staticmethod
    def nameSpace():    	return "PYRONAME:" + DEFAULT_HOLDER_NAME
    
    def isRunning(self):    	return self.running
		
    def loop(self):
    	self.running = True
#    	Pyro4.locateNS().register(DEFAULT_HOLDER_NAME, self.uri)
    	self.daemon.requestLoop(loopCondition=self.isRunning)
    	self.running =  False
    
    def stop(self):
        self.isRunning = False
        	
    def len(self):
    	return len(self.data)
		
def start():	
#    Pyro4.config.COMMTIMEOUT = 1
#    Pyro4.naming.startNSloop()
    holder = Holder()
    print holder.getUri()    
    holder.loop()	

def getHolderUri(uri):    holder = Pyro4.Proxy(uri)
    return holder
def getHolder():
    holder = Pyro4.Proxy(Holder.nameSpace())
    return holder
	
def main():        
    start()

if __name__ == "__main__":
    main()
		
