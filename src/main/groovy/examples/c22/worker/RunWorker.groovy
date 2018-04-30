package examples.c22.worker;

import jcsp.lang.*;
import jcsp.net2.*;
import jcsp.net2.tcpip.*;
import jcsp.net2.mobile.*;
import groovyJCSP.*;
import jcsp.userIO.*;

def w = Ask.Int ("Worker Number ( 3 upwards) ? ", 3, 20)
def emitterIP = Ask.string("Emitter Process IP? ")
def collectorIP = Ask.string("Collector Process IP? ")

def addr = new TCPIPNodeAddress ("127.0.0." + w,3000)
Node.getInstance().init (addr)
println "Worker IP address = ${addr.getIpAddress()}"

def fromEmitter = NetChannel.net2one(new CodeLoadingChannelFilter.FilterRX())
def fromEmitterLoc = fromEmitter.getLocation()
println "Worker: from Emitter channel Location - ${fromEmitterLoc.toString()}"

def toEmitterAddr = new TCPIPNodeAddress ( emitterIP, 3000)
def toEmitter = NetChannel.any2net(toEmitterAddr, 50 )

def toCollectorAddr = new TCPIPNodeAddress ( collectorIP, 3000)
def toCollector = NetChannel.any2net(toCollectorAddr, 50,  
                                    new CodeLoadingChannelFilter.FilterTX())

def base = new Worker ( toEmitter: toEmitter,
		   	  			          fromEmitterLoc: fromEmitterLoc,
                          fromEmitter: fromEmitter,
                          toCollector: toCollector,
                          baseId: w - 3 )
new PAR([base]).run()

