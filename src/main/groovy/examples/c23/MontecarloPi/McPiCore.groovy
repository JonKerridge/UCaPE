package examples.c23.MontecarloPi

import groovy_jcsp.*
import jcsp.lang.*





class McPiCore implements CSProcess {
	
	ChannelInput inChannel
	ChannelOutput outChannel
	
	void run() {
		def iterations = inChannel.read()
		def rng = new Random()
		int inQuadrant = 0
		1.upto(iterations) {
			def randomX = rng.nextFloat()
			def randomY = rng.nextFloat()
			if ( ((randomX * randomX)+(randomY * randomY)) < 1.0) inQuadrant = inQuadrant + 1
		}
		outChannel.write(inQuadrant)		
	}
}
