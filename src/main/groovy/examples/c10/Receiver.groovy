package examples.c10

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class Receiver implements CSProcess {
	
  ChannelInput fromElement
  ChannelOutput outChannel
  int element
  
  void run() {
    while (true) {
      def packet = fromElement.read()
      outChannel.write ("Received: " + packet.toString() + "\n")
    }
  }
}

