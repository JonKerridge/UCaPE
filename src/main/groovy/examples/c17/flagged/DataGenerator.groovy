package examples.c17.flagged

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.*
import jcsp.lang.*


class DataGenerator implements CSProcess {  
	
  def ChannelOutput outChannel
  def interval = 500
  
  void run() {
    println "Generator Started"
    def timer = new CSTimer()
    def i = 0
    while (true) {
      def v = new SystemData ( a: i, b: i+1)
      outChannel.write(v)
      i = i + 2
      timer.sleep(interval)
    }    
  }
}
