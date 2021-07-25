package examples.c10

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class ExtraElement implements CSProcess {

  ChannelInput fromRing
  ChannelOutput toRing
  
  void run () {
    def packet = new RingPacket (source:-1, destination:-1, value:-1, full: false )
    while (true) {
      toRing.write( packet )
      println "Extra Element 0 has written " + packet.toString()
      packet = (RingPacket) fromRing.read()
      println "Extra Element 0 has read " + packet.toString()
    }
  }
}
 
      
