package examples.c22.emitter

import jcsp.lang.*;

import examples.c22.universalClasses.*




class Emitter implements CSProcess {
		
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
			toWorkers[channelLoc].write(data[i])
		}
		// terminate each of the workers
		for ( i in 1..workers) {
			workerId = fromWorkers.read()
			toWorkers[workerId].write(new Sentinel())
		}
	}
}
