package examples.c07

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class BadC implements CSProcess {
	
  ChannelInput inChannel
  ChannelOutput outChannel 
   
  void run() {
    println "BadC: Starting"
	
    while (true) {
      println "BadC: outputting"
      outChannel.write(1)
      println "BadC: inputting"
      int i = inChannel.read()
      println "BadC: looping"
    }
  }
}

      