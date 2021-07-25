package examples.c09;

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovy_jcsp.*


class EventReceiver implements CSProcess { 
	 
  ChannelInput eventIn
  ChannelOutput eventOut
  
  void run() {
    while (true){
      eventOut.write(eventIn.read())
    }    
  }
}
