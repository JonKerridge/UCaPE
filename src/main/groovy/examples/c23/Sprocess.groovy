package examples.c23

import examples.c23.loaderObjects.Sentinel;
import examples.c23.loaderObjects.WorkerInterface
import groovyJCSP.*




class Sprocess  implements WorkerInterface {

	def ChannelInputList inChannels
	def ChannelOutputList outChannels
	
	def connect(inChannels, outChannels){
		this.inChannels = inChannels
		this.outChannels = outChannels
	}
		
	void run(){
		def boolean running = true
		def altChannels = new ChannelInputList()
		altChannels.append ( inChannels[1])
		altChannels.append ( inChannels[2])
		def sAlt = new ALT(altChannels)
		while (running){
			def o = inChannels[0].read()
			def index = sAlt.select()
			altChannels[index].read()	//signal
			outChannels[index].write(o)
			if ( o instanceof Sentinel) {
				index = 1 - index
				altChannels[index].read()	//signal
				outChannels[index].write(o)
				running = false
			}
		} // end while
		println "Sprocess terminated"
	} // end run
}
