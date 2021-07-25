package examples.c09

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class EventStream implements CSProcess {  
	
  int source = 0
  int initialValue = 0
  int iterations = 10
  ChannelOutput outChannel
    
  void run () {
    int i = initialValue
	
    1.upto(iterations) {
      def e = new EventData ( source: source, data: i )
      outChannel.write(e)
      i = i + 1
    }
    println "Source $source has finished"
  }
}

