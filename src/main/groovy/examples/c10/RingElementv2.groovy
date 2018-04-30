package examples.c10

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

class RingElementv2 implements CSProcess {
  
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
    def emptyPacket = new RingPacket ( source: -1, destination: -1 , value: -1 , full: false)
    def localBuffer = new RingPacket()
    def localBufferFull = false
    toRing.write ( emptyPacket )
    while (true) {
      def index = ringAlt.select(preCon)
      switch (index) {
        case RING:
          def ringBuffer = (RingPacket) fromRing.read()
          //println "REv2: Element ${element} has read ${ringBuffer.toString()} from ring"
          if ( ringBuffer.destination == element ) {  // packet for this node; full should be true
            toLocal.write(ringBuffer)     // write to local
            // now write either empty packet or a full localBuffer to ring
            if ( localBufferFull ) {
              //println "..REv2: Element ${element} writing ${localBuffer.toString()} to local"
              toRing.write ( localBuffer )
              preCon[LOCAL] = true          // allow another packet from Sender
              localBufferFull = false
            }
            else {
              toRing.write ( emptyPacket )
              //println "--REv2: Element ${element} written emptyPacket toRing"
            }
          }
          else {
            if ( ringBuffer.full ) {
              // packet for onward transmission to another element
              //println "++REv2: Element ${element} writing ${ringBuffer.toString()} onwards"
              toRing.write ( ringBuffer )
            }
            else {
              // have received an empty packet can output the localBuffer if full
              if ( localBufferFull ) {
                //println "==REv2: Element ${element} writing local ${localBuffer.toString()} to ring"
                toRing.write ( localBuffer )
                preCon[LOCAL] = true          // allow another packet from Sender
                localBufferFull = false
              }
              else {
                toRing.write ( emptyPacket )
                //println "##REv2: Element ${element} written emptyPacket toRing"
              }
            }
          }
          break
        case LOCAL:  
          localBuffer = fromLocal.read()
          preCon[LOCAL] = false             // stop any more Sends until buffer is emptied
          localBufferFull = true
          //println "@@REv2: Element ${element} read ${localBuffer.toString()} into localBuffer"
          break
      }  // end switch
    }
  }
}
      