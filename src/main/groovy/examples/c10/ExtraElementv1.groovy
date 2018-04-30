package examples.c10;


// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*

class ExtraElementv1 implements CSProcess {
	
  def ChannelInput fromRing
  def ChannelOutput toRing
  
  void run () {
    while (true) {
      def packet = (RingPacket) fromRing.read()
      println "Extra Element 0 has read " + packet.toString()
      toRing.write( packet )
      println "Extra Element 0 has written " + packet.toString()
    }
  }
}
