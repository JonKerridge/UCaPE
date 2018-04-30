package examples.c13

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class ReadClerk implements CSProcess {
	
  def ChannelInput cin
  def ChannelOutput cout
  def CrewMap data
  
  void run () {
    println "ReadClerk has started "
    while (true) {
      def d = new DataObject()
      d = cin.read()
      d.value = data.get ( d.location )
      println "RC: Reader ${d.pid} has read ${d.value} from ${d.location}"
      cout.write(d)
    }
  }
}

  