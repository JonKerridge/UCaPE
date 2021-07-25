package exercises.c4


import jcsp.lang.*
import groovy_jcsp.*

class ResetSuccessor implements CSProcess {
	  
  ChannelOutput outChannel
  ChannelInput  inChannel
  ChannelInput  resetChannel
	  
  void run () {
    def guards = [ resetChannel, inChannel  ]
    def alt = new ALT ( guards )
	while (true) {
	  // deal with inputs from resteChannel and inChannel
	  // use a priSelect
	}
  }
}
