package examples.c23

import examples.c23.loaderObjects.Sentinel;
import examples.c23.loaderObjects.WorkerInterface;

import jcsp.net2.tcpip.TCPIPNodeAddress
import jcsp.net2.*
import jcsp.net2.mobile.*
import netObjects.*
import groovyJCSP.*
import jcsp.lang.*

class Emitter implements WorkerInterface  {
	
	def ChannelInputList inChannels
	def ChannelOutputList outChannels
	
	def connect(inChannels, outChannels){
		this.inChannels = inChannels
		this.outChannels = outChannels
	}	
	
	void run(){
		//println "Emitter starting "
		def v = 1
		for ( i in 1 .. 10){
			def dObj = new DataObject (value: v)
			outChannels[0].write(dObj)
			v = v + 1
		}
		outChannels[0].write(new Sentinel())
		//println "Emitter Ending"
	}

}
