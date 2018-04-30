package examples.c12.fork

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*


class LazyButler implements CSProcess {
  
  def ChannelInputList enters
  def ChannelInputList exits
  
  void run() {
    def seats = enters.size()
    def allChans = []
    
    for ( i in 0 ..< seats ) { allChans << exits[i]  }
    for ( i in 0 ..< seats ) { allChans << enters[i] }
    
    def eitherAlt = new ALT ( allChans )
    
    while (true) {
      def i = eitherAlt.select()
      allChans[i].read()
    } // end while
  } //end run
} // end class
