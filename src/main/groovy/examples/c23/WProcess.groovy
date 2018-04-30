package examples.c23

import examples.c23.loaderObjects.Sentinel;
import examples.c23.loaderObjects.Signal;
import examples.c23.loaderObjects.WorkerInterface;

import groovyJCSP.*


class WProcess implements WorkerInterface {
	
	def ChannelInputList inChannels
	def ChannelOutputList outChannels
	def modifier = 100
	
	def connect(inChannels, outChannels){
		this.inChannels = inChannels
		this.outChannels = outChannels
	}
		
	void run(){
		def boolean running = true
		while (running){
			outChannels[0].write(new Signal())
			def o = inChannels[0].read()
			if ( o instanceof DataObject){
				o.value = o.value + modifier
				outChannels[1].write(o)
			}
			else {
				outChannels[1].write(new Sentinel())
				running = false
			} // end if
		} // end while 
		println "Worker $modifier terminated"
	} // end run

}
