##
#	@file:  lapps_pyro4_holder.py
#   @brief: provide memory holder using Pyro4
#	@url:	http://stackoverflow.com/questions/8508112/requestlooploopcondition-doesnt-release-even-after-loopcondition-is-false
#	@url:   http://pythonhosted.org/Pyro4/intro.html
#	@url:   http://pythonhosted.org/Pyro4/config.html
#   @ref:   http://stackoverflow.com/questions/1603109/how-to-make-a-python-script-run-like-a-service-or-daemon-in-linux
#   @ref:   http://blog.scphillips.com/2013/07/getting-a-python-script-to-run-in-the-background-as-a-service-on-boot/
#   @ref:   http://code.activestate.com/recipes/278731/
##

import sys, os, time, atexit, re, subprocess, inspect
from signal import SIGTERM
import Pyro4
import socket
import select
import sys
import Pyro4.core
import Pyro4.naming

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

### Pyro configuration, we set the server type here.
##
#
Pyro4.config.SERVERTYPE="thread"
#Pyro4.config.COMPRESSION = True
#Pyro4.config.SERIALIZER = 'pickle'
#Pyro4.config.SERIALIZERS_ACCEPTED.add('pickle')



### Constants are used.
##
#
DEFAULT_HOLDER_NAME = "Holder"


### Daemon super class for name server and pyro4 server.
##      After start ()
#       You can check python -m Pyro4.nsc list

class Daemon(object):
        """
        A generic daemon class.

        Usage: subclass the Daemon class and override the run() method
        """
        def __init__(self, pidfile, stdin='/dev/null', stdout='/dev/null', stderr='/dev/null'):
                self.stdin = stdin
                self.stdout = stdout
                self.stderr = stderr
                self.pidfile = pidfile

        def daemonize(self):
                """
                do the UNIX double-fork magic, see Stevens' "Advanced
                Programming in the UNIX Environment" for details (ISBN 0201563177)
                http://www.erlenstar.demon.co.uk/unix/faq_2.html#SEC16
                """
                try:
                        pid = os.fork()
                        if pid > 0:
                                # exit first parent
                                sys.exit(0)
                except OSError, e:
                        sys.stderr.write("fork #1 failed: %d (%s)\n" % (e.errno, e.strerror))
                        sys.exit(1)

                # decouple from parent environment
                os.chdir("/")
                os.setsid()
                os.umask(0)

                # do second fork
                try:
                        pid = os.fork()
                        if pid > 0:
                                # exit from second parent
                                sys.exit(0)
                except OSError, e:
                        sys.stderr.write("fork #2 failed: %d (%s)\n" % (e.errno, e.strerror))
                        sys.exit(1)

                # redirect standard file descriptors
                sys.stdout.flush()
                sys.stderr.flush()
                si = file(self.stdin, 'r')
                so = file(self.stdout, 'a+')
                se = file(self.stderr, 'a+', 0)
                os.dup2(si.fileno(), sys.stdin.fileno())
                os.dup2(so.fileno(), sys.stdout.fileno())
                os.dup2(se.fileno(), sys.stderr.fileno())

                # write pidfile
                atexit.register(self.delpid)
                pid = str(os.getpid())
                file(self.pidfile,'w+').write("%s\n" % pid)

        def delpid(self):
                os.remove(self.pidfile)

        def start(self):
                """
                Start the daemon
                """
                # Check for a pidfile to see if the daemon already runs
                try:
                        pf = file(self.pidfile, 'r')
                        pid = int(pf.read().strip())
                        pf.close()
                except IOError:
                        pid = None

                if pid:
                        message = "pidfile %s already exist. Daemon already running?\n"
                        sys.stderr.write(message % self.pidfile)
                        sys.exit(1)

                # Start the daemon
                self.daemonize()
                self.run()

        def status(self):
            if not os.path.exists(self.pidfile):
                return False

            try:
                pf = file(self.pidfile, 'r')
                pid = int(pf.read().strip())
                pf.close()
            except IOError:
                pid = None

            if not pid:
                return False

            # Try to ps find pid
            s = subprocess.Popen(["ps", "axw"], stdout=subprocess.PIPE)
            for x in s.stdout:
                if re.search(str(pid), x):
                    return True
                
            # If pid not running.    
            if os.path.exists(self.pidfile):
                os.remove(self.pidfile)    
            return False


        def stop(self):
                """
                Stop the daemon
                """
                # Get the pid from the pidfile
                try:
                        pf = file(self.pidfile,'r')
                        pid = int(pf.read().strip())
                        pf.close()
                except IOError:
                        pid = None

                if not pid:
                        message = "pidfile %s does not exist. Daemon not running?\n"
                        sys.stderr.write(message % self.pidfile)
                        return # not an error in a restart

                # Try killing the daemon process       
                try:
                        while 1:
                                os.kill(pid, SIGTERM)
                                time.sleep(0.1)
                except OSError, err:
                        err = str(err)
                        if err.find("No such process") > 0:
                                if os.path.exists(self.pidfile):
                                        os.remove(self.pidfile)
                        else:
                                print str(err)
                                sys.exit(1)

        def restart(self):
                """
                Restart the daemon
                """
                self.stop()
                self.start()

        def run(self):
                """
                You should override this method when you subclass Daemon. It will be called after the process has been
                daemonized by start() or restart().
                """
## End of class Daemon



class Holder(object):
    def __init__(self):
        self.data = {}
    
    def put(self, key, value):
        self.data[key] = value
        print "key="+key
        return value

    def get(self, key):
        print "key="+key
        return self.data[key]

    def getAndClear(self, key):
        print "key="+key
        val = self.data[key]
        del self.data[key]
        return val
    
    @staticmethod
    def nameSpace():
    	return DEFAULT_HOLDER_NAME

    def len(self):
    	return len(self.data)

## End of Holder

class HolderServer(Daemon):
    def __init__(self):
        pidFile = os.path.join(directory(), 'HolderServer.pid')
        stdoutFile = os.path.join(directory(), 'HolderServer.log')
        stderrFile = os.path.join(directory(), 'HolderServer.err')
        Daemon.__init__(self, pidFile, stdout = stdoutFile, stderr = stderrFile)
        
    def run(self):
        '''
            After start ()
            You can check:
            python -m Pyro4.nsc list        <----------------------------------------------
        '''
        hostname=socket.gethostname()
        nameserverUri, nameserverDaemon, broadcastServer = Pyro4.naming.startNS(host=hostname)
        pyrodaemon=Pyro4.core.Daemon(host=hostname)
        serveruri=pyrodaemon.register(Holder())
        nameserverDaemon.nameserver.register(Holder.nameSpace(), serveruri)

        while True:
            # print("Waiting for events...")
            # create sets of the socket objects we will be waiting on
            # (a set provides fast lookup compared to a list)
            nameserverSockets = set(nameserverDaemon.sockets)
            pyroSockets = set(pyrodaemon.sockets)
            rs=[broadcastServer]  # only the broadcast server is directly usable as a select() object
            rs.extend(nameserverSockets)
            rs.extend(pyroSockets)
            rs,_,_ = select.select(rs,[],[],3)
            eventsForNameserver=[]
            eventsForDaemon=[]
            for s in rs:
                if s is broadcastServer:
                    print("Broadcast server received a request")
                    broadcastServer.processRequest()
                elif s in nameserverSockets:
                    eventsForNameserver.append(s)
                elif s in pyroSockets:
                    eventsForDaemon.append(s)
            if eventsForNameserver:
                print("Nameserver received a request")
                nameserverDaemon.events(eventsForNameserver)
            if eventsForDaemon:
                print("Daemon received a request")
                pyrodaemon.events(eventsForDaemon)

        nameserverDaemon.close()
        broadcastServer.close()
        pyrodaemon.close()

## End of HolderServer

# if NOT running, then start.
def start():
    hs = HolderServer()
    if not hs.status():
        hs.start()

def stop():
    hs = HolderServer()
    hs.stop()
        
def put(key, val):
    # if HolderServer not running
    start()
    # Put (key, val)    
    holder = Pyro4.core.Proxy("PYRONAME:" + Holder.nameSpace())
    holder.put(key,val)    

def get(key):
    val = None
    try:
        # Get key 
        holder = Pyro4.core.Proxy("PYRONAME:" + Holder.nameSpace())
        val = holder.getAndClear(key)
    except Exception:
        val = None
    return val

if __name__ == "__main__":
     start()

    