package examples.c22.collector;

import jcsp.lang.*
import jcsp.net2.*
import jcsp.net2.tcpip.*
import jcsp.net2.mobile.*
import groovyJCSP.*
import jcsp.userIO.*;

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





