package examples.c22.emitter;
import jcsp.net2.NetChannel;

import jcsp.lang.*;
import jcsp.net2.*;
import jcsp.net2.tcpip.*;
import jcsp.net2.mobile.*;

import examples.c22.universalClasses.*



class CopyOfEmitterNet implements CSProcess {
	
	/*
	 * The changes required to EmitterNet result from the 
	 * fact that the InitObject now contains a net2 channel
	 * location of the input end of the channel to which 
	 * the process will write data objects.  NetLocations 
	 * is a map that holds the net2 locations indexed by
	 * the identity of the Base node, see lines 22 and 29.
	 */
	
	def fromWorkers
	def toWorkers
	def loops = 10
	def workers = 2
	def elements = 5
	
	void run(){
		def timer = new CSTimer()
		def data = []
		def workerId = 0
		def netLocations = [:]
		for ( i in 0 ..< loops) {
			data[i] = new TestObject (elements, i)
		}
		println "Emitter: Data Generated, Loops: $loops, Elements per object: $elements for $workers Workers"
		for ( i in 1..workers) {
			def initLoc = (InitObject)fromWorkers.read()
			netLocations.put (initLoc.id, NetChannel.one2net(initLoc.channelAddress, 50,  new CodeLoadingChannelFilter.FilterTX()))
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
			println "Emitter: Request received from $workerId"
			channelLoc = netLocations.get (workerId)
			// in this version it is a write to a net2 channel
			println "Emitter: writing data $i to $workerId "
			println " ${data[i].display(timer.read())}"
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
