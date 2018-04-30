package examples.c22.worker;

import jcsp.lang.*;
import examples.c22.universalClasses.*


class CopyOfSendOutput implements CSProcess {
	
	/*
	 * The SendOutput process reads a value from its 
	 * workerFinished internal channel.  In the case 
	 * of a terminating Sentinel the value is just 
	 * written to the toCollector channel.  Otherwise, 
	 * the value will be the index of the data object 
	 * in the sharedData list that has just been processed.  
	 * The process then writes this data object to the 
	 * Collector process using the toCollector channel.
	 */
	
	def workerFinished
	def toCollector
	def sharedData
	
	void run(){
		def index = -1
		def running = true
		while (running) {
			def o = workerFinished.read()
			if ( o instanceof Sentinel){
				running = false
				toCollector.write(o)
			}
			else {
				index = (Integer) o.intValue()
				toCollector.write(sharedData[index])
			}
		}
	}

}
