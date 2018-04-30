package examples.c23.MontecarloPi

import examples.c23.loaderObjects.WorkerInterface;

import groovyJCSP.ChannelInputList;
import groovyJCSP.ChannelOutputList;

 

class McPiEmitter implements WorkerInterface {
	
	def ChannelInputList inChannels 
	def ChannelOutputList outChannels 
	def workers = 1
	def iterations = 192
	
	def connect(inChannels, outChannels){
		this.inChannels = inChannels
		this.outChannels = outChannels
	}
	
	void run(){
		println "running McPiEmitter"
		for ( w in 0 ..< workers) outChannels[w].write(iterations / workers)
	}
}
