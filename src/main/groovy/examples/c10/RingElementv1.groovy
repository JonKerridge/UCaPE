package examples.c10

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

class RingElementv1 implements CSProcess {
  
  def ChannelInput fromRing
  def ChannelOutput toRing
  def ChannelInput fromLocal
  def ChannelOutput toLocal
  def int element
  
  void run () {
    def RING = 0
    def LOCAL= 1
    def ringAlt = new ALT ( [ fromRing, fromLocal ] )
    def preCon = new boolean[2]
    preCon[RING] = true
    preCon[LOCAL] = true
    while (true) {
      def index = ringAlt.select(preCon)
      switch (index) {
        case RING:
          def packet = (RingPacket) fromRing.read()
          println "REv1: Element ${element} has read ${packet.toString()}"
          if ( packet.destination == element ) {  // packet for this node
            if ( packet.full ) {        // packet from elsewhere
              println "--REv1: Element ${element} writing ${packet.toString()} to local"
              toLocal.write(packet.copy())     // write to local
              packet.destination = packet.source
              packet.source = element
              packet.full = false
              println "**REv1: Element ${element} writing empty ${packet.toString()} to source"
              toRing.write(packet)      //send modifed packet back to source
            }
            else {    //returned empty packet
              println "##REv1: Element ${element} received empty ${packet.toString()}"
              preCon[LOCAL] = true    // can accept a packet from local node
            }
          }
          else { // packet for another node
            println "..REv1: Element ${element} writing ${packet.toString()} onwards"
            toRing.write (packet) 
          }
          break
        case LOCAL:
          def packet = (RingPacket) fromLocal.read()
          println "++REv1: Element ${element} writing local ${packet.toString()} to ring"
          toRing.write (packet)
          preCon[LOCAL] = false     // stop local from sending any more packets
          break
      }
    }
  }
}

    
        