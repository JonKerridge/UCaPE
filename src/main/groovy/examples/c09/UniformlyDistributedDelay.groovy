package examples.c09
  
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class UniformlyDistributedDelay implements CSProcess {  
	
  ChannelInput inChannel
  ChannelOutput outChannel
  int minTime = 100
  int maxTime = 1000  
  
  void run () {
    def timer = new CSTimer()
    def rng = new Random()
	
    while (true) {
      def v = inChannel.read().copy()
      def delay = minTime + rng.nextInt ( maxTime - minTime )
      timer.sleep (delay)
      outChannel.write( v )
    }
  }
}  
