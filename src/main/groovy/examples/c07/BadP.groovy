package examples.c07

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class BadP implements CSProcess {
	
  ChannelInput inChannel
  ChannelOutput outChannel  
  
  def void run() {
    println "BadP: Starting"
	
    while (true) {
      println "BadP: outputting"
      outChannel.write(1)
      println "BadP: inputting"
      int i = inChannel.read()
      println "BadP: looping"
    }
  }
}

      