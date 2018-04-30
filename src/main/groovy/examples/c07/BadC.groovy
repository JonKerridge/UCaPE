package examples.c07

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class BadC implements CSProcess {
	
  def ChannelInput inChannel
  def ChannelOutput outChannel 
   
  void run() {
    println "BadC: Starting"
	
    while (true) {
      println "BadC: outputting"
      outChannel.write(1)
      println "BadC: inputting"
      def i = inChannel.read()
      println "BadC: looping"
    }
  }
}

      