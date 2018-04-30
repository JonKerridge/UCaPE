package examples.c13

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class WriteClerk implements CSProcess {
	
  def ChannelInput cin
  def ChannelOutput cout
  def CrewMap data
  
  void run () {
    println "WriteClerk has started "
    while (true) {
      def d = new DataObject()
      d = cin.read()
      data.put ( d.location, d.value )
      println "WC: Writer ${d.pid} has written ${d.value} to ${d.location}"
      cout.write(d)
    }
  }
}

  