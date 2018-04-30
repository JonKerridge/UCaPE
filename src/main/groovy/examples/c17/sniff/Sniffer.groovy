package examples.c17.sniff

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*


class Sniffer implements CSProcess{  
	
  def ChannelInput fromSystemCopy
  def ChannelOutput toComparator
  def sampleInterval = 10000
  
  void run() {
    def TIME = 0
    def INPUT = 1
    def timer = new CSTimer()
    def snifferAlt = new ALT([timer, fromSystemCopy])
    def timeout = timer.read() + sampleInterval
    timer.setAlarm(timeout)
	
    while (true) {
      def index = snifferAlt.select()
      switch (index) {
        case TIME:
          toComparator.write(fromSystemCopy.read())
          timeout = timer.read() + sampleInterval
          timer.setAlarm(timeout)
          break
        case INPUT:
          fromSystemCopy.read()
          break
      }
    } // end while    
  }
}
