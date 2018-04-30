package examples.c22.worker;

import jcsp.lang.*;
import examples.c22.universalClasses.*


/**
 * @author Jon Kerridge
 *
 */

class CopyOfWorker implements CSProcess {
	
	/*
	 * The Worker process reads the index of the sharedData upon 
	 * which it is to operate.  Unless the input is the terminating 
	 * Sentinel it undertakes the manipulation of the data object 
	 * using the manipulate method defined in the TestObject 
	 * that is an abstract method of the ManipulateInterface.  
	 * Once the work is complete it writes the index of the now 
	 * processed data object in the sharedData list to the 
	 * SendOuput process using the internal channel workCompleted. 
	 */
	
	def workOn	
	def workCompleted
	def workerId
	def sharedData
	
	void run(){
		def index = -1
		def running = true
		while (running) {
			def o = workOn.read()
			if ( o instanceof Sentinel) {
				running = false
				workCompleted.write(o)
			}
			else {
				index = (Integer)o.intValue()
				sharedData[index].manipulate(workerId)
				workCompleted.write(index)								
			}
		}
	}

}
