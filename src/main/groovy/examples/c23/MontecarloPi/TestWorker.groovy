package examples.c23.MontecarloPi

import jcsp.lang.*
import groovyJCSP.*

class TestWorker implements CSProcess {
	
	def ChannelInput inChannel
	def ChannelOutput outChannel
	def cores = 1
	
	void run(){
		println "running testWorker"
		def M2C = Channel.one2oneArray(cores)
		def C2M = Channel.one2oneArray(cores)
		def toCores = new ChannelOutputList(M2C)
		def fromCores = new ChannelInputList(C2M)
		def coreNetwork = (0 ..< cores).collect{ c ->
			return new McPiCore( inChannel: M2C[c].in(), 
								 outChannel: C2M[c].out() ) }
		def manager = new McPiManager ( inChannel: inChannel,
										outChannel: outChannel,
										toCores: toCores,
										fromCores: fromCores)
		new PAR(coreNetwork + [manager]).run()
	}

}
