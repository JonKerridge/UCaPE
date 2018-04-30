package examples.c17.flagged

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.*
import jcsp.lang.*


class SampledNetwork implements CSProcess {  
	
  def ChannelInput inChannel
  def ChannelOutput outChannel
  
  void run() {
	  
    while (true) {
      def v = inChannel.read()
      v.c = v.a + v.b
      outChannel.write(v)
    }    
  }
}
