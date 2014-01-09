


############################################################################################################




############################################################################################################

Lapps Common IO (for Python) Version 0.04

DONE:
1.

TODO:




############################################################################################################
Jan 7, 2014

Lapps Common IO (for Python) Version 0.03

DONE:
1. provide Java (edu.brandeis.cs.python.utils.PythonRunner) integration
   1) Allows replacement of parameters
   2) Allows reference of global variable
   
TODO:
1. Provide Java pickle access
2. Provide Pyro4 interface
   


############################################################################################################

Dec 18, 2013



Lapps Common IO (for Python) Version 0.02

DONE:
1. Provide Debugging and Unit Testing 
   1) Rewrite into Class to make the codes neat.
   2) Allow debugging and unit testing.

2.  



TODO:
1. Provide enhanced Section Item 1) Requires
   1) allows parameters replacesments for all the sections
   2) allows existing return value checking

2. Provide Pyro4 interfaces


############################################################################################################


Dec 02, 2013

Lapps Common IO (for Python) Version 0.01
DONE:
1. Design an Configuration Interface to Call Python Methods.
   We believe there are 8 steps to configuration a python runner, and we call this as "SECTION"
   Section Item  1) Requires:  what we need to do before running this function
   Section Item  2) Module(Python File): which module or Python file should be loaded for running.
   Section Item  3) Loads: which existing python pickle objects should bed loaded.
   Section Item  4) Globals: which global variables in this module should be preset before running.
   Section Item  5) Method: which method for this section.
   Section Item  6) Args: which arguments will be sequencely assigned.
   Section Item  7) Return: provide a reference for the return value
   Section Item  8) Dumps: dumps needed reference into pickle directory

2. Benefits of Configuration Python Method Caller.
   1) Using existing Python Modules instead of rewriting another python file, which saving a lot works.
   2) Provide python workflows


3. Examples of the Configuration Setting


TODO:
1. Make codes neat.
2. Provide unit testting

