package examples.c09

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class EventPrompter implements CSProcess {  
	
  def ChannelInput inChannel
  def ChannelOutput getChannel
  def ChannelOutput outChannel  
  
  void run () {
    def s = 1
    while (true) {
      getChannel.write(s)
      def e = inChannel.read().copy()
      outChannel.write( e )
    }
  }
}

