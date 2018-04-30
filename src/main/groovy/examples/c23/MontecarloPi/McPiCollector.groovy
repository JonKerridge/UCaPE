package examples.c23.MontecarloPi

import examples.c23.loaderObjects.WorkerInterface;

import groovyJCSP.ChannelInputList;
import groovyJCSP.ChannelOutputList;



class McPiCollector implements WorkerInterface {
	
	def ChannelInputList inChannels
	def ChannelOutputList outChannels
	def workers = 1
	def iterations = 192
	def cores = 1
	
	def connect(inChannels, outChannels){
		this.inChannels = inChannels
		this.outChannels = outChannels
	}
	
	void run(){
		println "running McPiCollector"
		def quadSum = 0
		for (w in 0 ..< workers) quadSum = quadSum + inChannels[w].read()
		def pi = quadSum / iterations * 4
		println "The value of pi is $pi"
		println "Workers: $workers, Iterations: $iterations, Cores : $cores"
	}
}
