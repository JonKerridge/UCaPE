package examples.c17.flagged

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovy_jcsp.*
import jcsp.lang.*


class SampledNetwork implements CSProcess {  
	
  ChannelInput inChannel
  ChannelOutput outChannel
  
  void run() {
	  
    while (true) {
      def v = inChannel.read()
      v.c = v.a + v.b
      outChannel.write(v)
    }    
  }
}
