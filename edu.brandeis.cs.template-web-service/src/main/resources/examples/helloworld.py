##
#    @file:    example.py
#    @brief:    describe a simple example for configure an running Python function dynamically.
#    @example:    append("hello", "world")
#
##
import sys, os

hello = "hello"

def append(str1, str2):
    return str1 + " " + str2

def say(str):
    return hello + " " + str



'''

# configuration examples.

[helloworld.prepare]
Dumps="hello":myhello, "world":myworld

[helloworld.append]
Loads=hel:mhello, wor:myworld
PythonFile=example/helloworld.py
Method=append
Args=hel, wor
Return=hello_world
Dumps=hello_world:myhelloworld

[helloworld.say]
Loads=he:myhello
Globals=hello:he
PythonFile=example/helloworld.py
Method=say
Args="world"
Return=hello_world2
Dumps=hello_world2:helloworld/say

[helloworld]
Requires=helloworld.prepare, helloworld.append,  helloworld.say

'''