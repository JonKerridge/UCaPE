package exercises.c2

import jcsp.lang.*

class ListToStream implements CSProcess{
	
	ChannelInput inChannel
	ChannelOutput outChannel
	
	void run (){
		def inList = inChannel.read()
		while (inList[0] != -1) {
			// hint: output	list elements as single integers
		}
		outChannel.write(-1)
	}
}