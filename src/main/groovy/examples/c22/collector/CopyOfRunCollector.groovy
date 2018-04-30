package examples.c22.collector;

import jcsp.lang.*;
import jcsp.net2.*;
import jcsp.net2.tcpip.*;
import jcsp.net2.mobile.*;

import groovyJCSP.*;

import jcsp.userIO.*;


/*
 * RunCollector creates port 3000 on its node.  The node�s IP address 
 * is then printed as this will be needed to inform the Base nodes.  
 * The any2one channel fromWorkers is created as a net2one channel 
 * because this is the reading end of the channel.  
 * Note that it uses the CodeLoadingChannelFilter.FilterRX() (line 27) structuring 
 * mechanism.  The unaltered Collector process is then invoked.  The 
 * number of workers specified in in the script RunEmitter must be the 
 * same as that specified in RunCollector.
 */

def nodeAddr = new TCPIPNodeAddress("127.0.0.2",3000)
Node.getInstance().init (nodeAddr)
println "Collector IP address = ${nodeAddr.getIpAddress()}"

def fromWorkers = NetChannel.net2one(new CodeLoadingChannelFilter.FilterRX())

def fromWorkersLoc = fromWorkers.getLocation()

println "Collector: from Workers channel Location - ${fromWorkersLoc.toString()}"

def workers = Ask.Int ("Number of workers? ", 1, 20)


def collector = new Collector ( fromWorkers: fromWorkers, 
								workers: workers)

new PAR([collector]).run()





