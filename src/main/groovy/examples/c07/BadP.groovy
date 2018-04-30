package examples.c07

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class BadP implements CSProcess {
	
  def ChannelInput inChannel
  def ChannelOutput outChannel  
  
  def void run() {
    println "BadP: Starting"
	
    while (true) {
      println "BadP: outputting"
      outChannel.write(1)
      println "BadP: inputting"
      def i = inChannel.read()
      println "BadP: looping"
    }
  }
}

      