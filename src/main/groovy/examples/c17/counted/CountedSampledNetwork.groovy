package examples.c17.counted

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.*
import jcsp.lang.*


class CountedSampledNetwork implements CSProcess {
	
  def ChannelOutput outChannel
  def ChannelInput inChannel  
  
  void run() {
    def timer = new CSTimer()
    while (true){
      def v = inChannel.read()
      timer.sleep(250)
      outChannel.write (v*2)
    }
  }
}
