package examples.c22.worker;

import jcsp.lang.*;
import examples.c22.universalClasses.*





class SendOutput implements CSProcess {
	
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
