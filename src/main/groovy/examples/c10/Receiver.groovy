package examples.c10

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class Receiver implements CSProcess {
	
  def ChannelInput fromElement
  def ChannelOutput outChannel
  def int element
  
  void run() {
    while (true) {
      def packet = fromElement.read()
      outChannel.write ("Received: " + packet.toString() + "\n")
    }
  }
}

