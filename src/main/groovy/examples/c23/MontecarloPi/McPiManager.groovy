package examples.c23.MontecarloPi

import groovy_jcsp.*
import jcsp.lang.*





class McPiManager implements CSProcess {
	
	ChannelInput inChannel
	ChannelOutput outChannel
	ChannelOutputList toCores
	ChannelInputList fromCores
	
	void run() {
		def cores = fromCores.size()
		def iterations = inChannel.read()
		for ( c in 0..< cores) toCores[c].write(iterations / cores)
		def quadSum = 0
		for ( c in 0..< cores) quadSum = quadSum + fromCores[c].read()
		outChannel.write(quadSum)
	}	
}
