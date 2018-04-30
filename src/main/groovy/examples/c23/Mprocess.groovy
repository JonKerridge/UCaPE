package examples.c23



import examples.c23.loaderObjects.Sentinel;
import examples.c23.loaderObjects.WorkerInterface;

import groovyJCSP.*

class Mprocess  implements WorkerInterface {

	def ChannelInputList inChannels
	def ChannelOutputList outChannels
	
	def connect(inChannels, outChannels){
		this.inChannels = inChannels
		this.outChannels = outChannels
	}
		
	void run(){
		def boolean running = true
		def terminated = 0
		def mAlt = new ALT(inChannels)
		while (running){
			def index = mAlt.select()
			def o = inChannels[index].read()	//value
			if ( o instanceof Sentinel) {
				terminated = terminated + 1
				if ( terminated == inChannels.size())
					running = false
			}
			else {
				outChannels[0].write(o)
			}
		} // end while
		outChannels[0].write(new Sentinel())
		println "Mprocess terminated"
	} // end run
}
