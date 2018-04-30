package exercises.c9

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

class Manager1Only implements CSProcess {
	
	def ChannelInputList inputs
	def ChannelOutputList outputs
	def ChannelInput fromBlender
	def ChannelOutput toBlender
	def int hoppers = 3

	void run(){
		def alt = new ALT(inputs)
		while (true) {
			// read 1 from one of the three hoppers
			// now read 1 from the blender
			// now send a response to the accepted hopper and the blender
			// now read terminating 2 from blender
			// and the terminating 2 from the accepted hopper
			// now send a response to the hopper and the blender
		}
	}	
}
