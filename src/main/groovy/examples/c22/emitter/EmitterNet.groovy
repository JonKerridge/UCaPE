package examples.c22.emitter;
import jcsp.net2.NetChannel;

import jcsp.lang.*;
import jcsp.net2.*;
import jcsp.net2.tcpip.*;
import jcsp.net2.mobile.*;
import examples.c22.universalClasses.*

class EmitterNet implements CSProcess {
	
	def fromWorkers
	def toWorkers
	def loops = 10
	def workers = 2
	def elements = 5
	
	void run(){
		def data = []
		def workerId = 0
		def netLocations = [:]
    // create the data
		for ( i in 0 ..< loops) {
			data[i] = new TestObject (elements, i)
		}
		println "Emitter: Data Generated, Loops: $loops, " +
            "Elements per object: $elements for $workers Workers"
    // receive an InitObject from each worker
		for ( i in 1..workers) {
			def initLoc = (InitObject)fromWorkers.read()
			netLocations.put (initLoc.id, NetChannel.one2net(initLoc.channelAddress, 50,  
                        new CodeLoadingChannelFilter.FilterTX()))
		}
		println "Emitter: $workers Workers have registered"
		// send each worker a synchronisation signal
		def channelLoc = null
		for ( i in 0 ..< workers) {
			channelLoc = netLocations.get (i)
			channelLoc.write(new Signal())
		}
		println "Emitter: $workers Workers have synchronised"
		// wait for a request from a worker and then send them
		for ( i in 0 ..< loops) {
			workerId = ((Signal)fromWorkers.read()).signal
			channelLoc = netLocations.get (workerId)
			channelLoc.write(data[i])
		}
		// terminate each of the workers
		for ( i in 1..workers) {
			workerId = ((Signal)fromWorkers.read()).signal
			channelLoc = netLocations.get (workerId)
			channelLoc.write(new Sentinel())
		}
	}
}
