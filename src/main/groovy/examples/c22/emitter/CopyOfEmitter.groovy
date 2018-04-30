package examples.c22.emitter

import jcsp.lang.*;

import examples.c22.universalClasses.*

class CopyOfEmitter implements CSProcess {
	
	/*
	 * The Emitter process creates a list (data[]) of TestObjects 
	 * which are created BEFORE the system is executed.  The number 
	 * of data elements in a TestObject can be varied with the elements property.  
	 * The number of data objects is varied using the loops property.  
	 * These properties together with the number of worker or Base Nodes 
	 * can be varied at run time.
	 * 
	 * The Emitter process then receives a communication from each Base node 
	 * which registers that node with the Emitter.  Once all the Base nodes 
	 * have registered, the Emitter process sends a go signal to each Base node. 
	 * After which it can send work to each of the Base nodes as they 
	 * become ready to process work.  The interaction requires the Base node 
	 * to send a request to the Emitter which then responds with the next work object.  
	 * Once all the work has been sent for processing, the Emitter node sends a 
	 * terminating Sentinel object to each Base node.
	 * 
	 */
	
	def fromWorkers
	def toWorkers
	def loops = 10
	def workers = 2
	def elements = 5
	
	void run(){
		def data = []
		def workerId = 0
		def netLocations = [:]
		for ( i in 0 ..< loops) {
			data[i] = new TestObject (elements)
		}
		println "Emitter: Data Generated"
		for ( i in 1..workers) {
			def initLoc = (InitObject) fromWorkers.read()
			netLocations.put (initLoc.id, initLoc.channelAddress)
		}
		println "Emitter: Workers have registered"
		def channelLoc = -1
		for ( i in 0 ..< workers) {
			channelLoc = netLocations.get (i)
			toWorkers[channelLoc].write(new Signal())
		}
		println "Emitter: Workers have synchronised"
		// wait for a request from a worker and then send then work
		for ( i in 0 ..< loops) {
			workerId = fromWorkers.read()
			channelLoc = netLocations.get (workerId)
			// in the final version this will be a write to a net2 channel
			toWorkers[channelLoc].write(data[i])
		}
		// terminate each of the workers
		for ( i in 1..workers) {
			workerId = fromWorkers.read()
			toWorkers[workerId].write(new Sentinel())
		}
	}
}
