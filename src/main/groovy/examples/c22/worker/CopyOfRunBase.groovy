package examples.c22.worker;

import jcsp.lang.*;
import jcsp.net2.*;
import jcsp.net2.tcpip.*;
import jcsp.net2.mobile.*;
import groovyJCSP.*;
import jcsp.userIO.*;


/*
 * Initially RunBase asks for the integer identity of the 
 * worker, (0 ..< workers).  The user then has to enter the IP 
 * addresses of the Emitter and Collector nodes.  The script then 
 * creates a port on this node numbered from 3002 indexed by the 
 * worker identity.  The net2 input channel fromEmitter is then
 * created as a code loading channel and its location determined 
 * as fromEmitterLoc (line 21 and 22).  Now the any end of the net2
 * channel that connects the Base node to the Emitter and Collector 
 * nodes are created.  We know that the input end of the Base node 
 * to Emitter node is located at port 3000 on the Emitter node (line 25). 
 * We can then create an any2net net2 channel called toEmitter (line 26).
 * This is created on virtual channel number 50.  A particular port 
 * on a node can have a large number of virtual channels created.  
 * Thus only one port on a node is required for a connection between 
 * any two nodes that may involve many channels.   
 * 
 * The same mechanism is used to create the net2 any2net channels
 * from the Base node to the Collector node (lines 28 and 29).  
 * The process BaseNet is then invoked.
 */

def w = Ask.Int ("Worker Number ( 3 upwards) ? ", 3, 20)
def emitterIP = Ask.string("Emitter Process IP? ")
def collectorIP = Ask.string("Collector Process IP? ")

def addr = new TCPIPNodeAddress ("127.0.0." + w,3000)
Node.getInstance().init (addr)
println "Base Worker IP address = ${addr.getIpAddress()}"

def fromEmitter = NetChannel.net2one(new CodeLoadingChannelFilter.FilterRX())
def fromEmitterLoc = fromEmitter.getLocation()
println "Worker: from Emitter channel Location - ${fromEmitterLoc.toString()}"

def toEmitterAddr = new TCPIPNodeAddress ( emitterIP, 3000)
def toEmitter = NetChannel.any2net(toEmitterAddr, 50 )

def toCollectorAddr = new TCPIPNodeAddress ( collectorIP, 3000)
def toCollector = NetChannel.any2net(toCollectorAddr, 50,  new CodeLoadingChannelFilter.FilterTX())

def base = new Worker ( toEmitter: toEmitter,
		   	  			 fromEmitterLoc: fromEmitterLoc,
		   	  			 fromEmitter: fromEmitter,
		   	  			 toCollector: toCollector,
		   	  			 baseId: w - 3 )
new PAR([base]).run()

