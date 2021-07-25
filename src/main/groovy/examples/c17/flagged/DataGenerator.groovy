package examples.c17.flagged

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovy_jcsp.*
import jcsp.lang.*


class DataGenerator implements CSProcess {  
	
  ChannelOutput outChannel
  long interval = 500
  
  void run() {
    println "Generator Started"
    def timer = new CSTimer()
    int i = 0
    while (true) {
      def v = new SystemData ( a: i, b: i+1)
      outChannel.write(v)
      i = i + 2
      timer.sleep(interval)
    }    
  }
}
