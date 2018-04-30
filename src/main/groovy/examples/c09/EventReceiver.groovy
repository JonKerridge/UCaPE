package examples.c09;

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*


class EventReceiver implements CSProcess { 
	 
  def ChannelInput eventIn
  def ChannelOutput eventOut
  
  void run() {
    while (true){
      eventOut.write(eventIn.read())
    }    
  }
}
