package examples.c22.worker;

import jcsp.lang.*;
import examples.c22.universalClasses.*





class GetInput implements CSProcess {
	
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
