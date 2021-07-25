package examples.c10

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class Sender implements CSProcess {
	
  ChannelOutput toElement
  int element
  int nodes
  int iterations = 12
  
  void run() {
    for ( i in 1 .. iterations ) {
      def dest = (i % (nodes) ) + 1
      if ( dest != element ) {
        def packet = new RingPacket ( source: element, destination: dest , value: (element * 100) + i , full: true)
        toElement.write(packet)
        println "Sender ${element}: has sent " + packet.toString()
      }
    }
  }
}

    
