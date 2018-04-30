package examples.c12.fork

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class Fork implements CSProcess {
  
  def ChannelInput left
  def ChannelInput right
  
  void run () {
    def fromPhilosopher = [left, right]
    def forkAlt = new ALT ( fromPhilosopher )
    while (true) {
      def i = forkAlt.select()
      fromPhilosopher[i].read()      //pick up fork i
      fromPhilosopher[i].read()      //put down fork i
    }
  }
}

      
    