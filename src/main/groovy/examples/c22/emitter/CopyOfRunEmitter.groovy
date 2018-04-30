package examples.c22.emitter;

import jcsp.lang.*;
import jcsp.net2.*;
import jcsp.net2.tcpip.*;
import jcsp.net2.mobile.*;

import groovyJCSP.*;

import jcsp.userIO.*;


/*
 * RunEmitter creates port 3000 on its node.  The node's 
 * IP address is then printed as this will be needed by 
 * the RunBase instances created last.  The next few 
 * interactions specify the parameters of this execution 
 * of the system.  The process EmitterNet is then invoked.
 */

def nodeAddr = new TCPIPNodeAddress("127.0.0.1",3000)
Node.getInstance().init (nodeAddr)
println "Emitter IP address = ${nodeAddr.getIpAddress()}"

def fromWorkers = NetChannel.net2one()

def fromWorkersLoc = fromWorkers.getLocation()

println "Emitter: from Workers channel Location - ${fromWorkersLoc.toString()}"

def workers = Ask.Int ("Number of workers? ", 1, 17)
def loops = Ask.Int ("Number of data objects to send? ", 1, 1000000)
def elements = Ask.Int ("Number of elements in each TestObject? ", 1, 200)


def emit = new EmitterNet ( fromWorkers: fromWorkers,
						    loops: loops,
						    workers: workers,
							elements: elements )
new PAR([emit]).run()

	



