package examples.c18.net2

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovy_jcsp.*
import jcsp.lang.*
import jcsp.userIO.*
 
int nodes = Ask.Int ("Number of Nodes ? ", 1, 9)
int iterations = Ask.Int ("Number of Iterations ? ", 1, 9)
def String initialValue = Ask.string ( "Initial List Value ? ")

def ring = Channel.one2oneArray(nodes+1)

def processNodes = (1 ..< nodes).collect { 
	i -> new ProcessNode ( inChannel: ring[i].in(),
                           outChannel: ring[i+1].out(),
                           nodeId: i) 
   } 

processNodes << new ProcessNode ( inChannel: ring[nodes].in(),
                                   outChannel: ring[0].out(),
                                   nodeId: nodes)

def rootNode = new Root ( inChannel: ring[0].in(), 
                           outChannel: ring[1].out(),
                           iterations: iterations,
                           initialValue: initialValue)

def network = processNodes << rootNode

new PAR ( network ).run()
