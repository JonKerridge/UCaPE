package examples.c12.fork

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class Fork implements CSProcess {
  
  ChannelInput left
  ChannelInput right
  
  void run () {
    def fromPhilosopher = [left, right]
    def forkAlt = new ALT ( fromPhilosopher )
    while (true) {
      int i = forkAlt.select()
      fromPhilosopher[i].read()      //pick up fork i
      fromPhilosopher[i].read()      //put down fork i
    }
  }
}

      
    