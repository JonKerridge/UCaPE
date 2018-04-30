package examples.c09
  
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class UniformlyDistributedDelay implements CSProcess {  
	
  def ChannelInput inChannel
  def ChannelOutput outChannel
  def int minTime = 100
  def int maxTime = 1000  
  
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
