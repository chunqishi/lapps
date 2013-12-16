# saved as client.py
import Pyro4, UserDict, time

uri=raw_input("What is the Pyro uri of your hash map? ").strip()
ud=Pyro4.Proxy(uri)          # get a Pyro proxy to the greeting object

ud.data[time.strftime("%x")]=time.strftime("%X")
print ud  # call method normally
