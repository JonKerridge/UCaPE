package exercises.c9

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*

class ManagerAll3 implements CSProcess {
	
	ChannelInputList inputs
	ChannelOutputList outputs
	int hoppers = 3

	void run(){
		while (true) {
			// read 1 from the three hoppers
			// now read 1 from the blender
			// now send a response to the hoppers and the blender
			// now read terminating 2 from blender
			// and read the termninating 2 from the hoppers
			// now send a response to the hoppers and the blender
		}
	}	
}
