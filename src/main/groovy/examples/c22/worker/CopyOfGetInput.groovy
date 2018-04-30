package examples.c22.worker;

import jcsp.lang.*;
import examples.c22.universalClasses.*


class CopyOfGetInput implements CSProcess {
	
	/*
	 * The GetInput process writes a request to the Emitter 
	 * asking for data to process.  It then reads either a 
	 * terminating Sentinel from the Emitter or a data object 
	 * which is written to the next available sharedData list element.  
	 * The process then writes the index of the data object in 
	 * sharedData to the Worker process using the toWorker internal channel.  
	 * Note that this write operation may be delayed because the Worker 
	 * process may still be processing the previous data object.
	 */
	
	def toEmitter
	def baseId
	def fromEmitter
	def toWorker
	def sharedData
	
	void run(){
		def index = 0
		def running = true
		while (running) {
			toEmitter.write(new Signal(signal: baseId))
			def o = fromEmitter.read()
			if ( o instanceof Sentinel){
				running = false
				toWorker.write(o)
			}
			else {
				sharedData[index] = o
				toWorker.write(index)
				index = (index + 1)%3
			}
		}
	}
}
