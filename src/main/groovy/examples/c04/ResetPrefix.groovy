package examples.c04

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class ResetPrefix implements CSProcess {
  
  int prefixValue = 0
  ChannelOutput outChannel
  ChannelInput  inChannel
  ChannelInput  resetChannel
  
  void run () {
    def guards = [ resetChannel, inChannel  ]
    def alt = new ALT ( guards )
    outChannel.write(prefixValue)
    while (true) {
      def index = alt.priSelect()
      if (index == 0 ) {    // resetChannel input
        def resetValue = resetChannel.read()
		inChannel.read()
        outChannel.write(resetValue)
      }
      else {    //inChannel input 
        outChannel.write(inChannel.read())        
      }
    }
  }
}
