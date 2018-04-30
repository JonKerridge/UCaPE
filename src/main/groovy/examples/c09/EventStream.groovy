package examples.c09

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class EventStream implements CSProcess {  
	
  def int source = 0
  def int initialValue = 0
  def int iterations = 10
  def ChannelOutput outChannel
    
  void run () {
    def i = initialValue
	
    1.upto(iterations) {
      def e = new EventData ( source: source, data: i )
      outChannel.write(e)
      i = i + 1
    }
    println "Source $source has finished"
  }
}

