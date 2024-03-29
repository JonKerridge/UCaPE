package examples.c17.flagged

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import groovy_jcsp.*
import jcsp.lang.*

class Gatherer implements CSProcess {  
	
  ChannelInput inChannel
  ChannelOutput outChannel
  ChannelOutput gatheredData
  
  void run(){	  
    while (true){		
      def v = inChannel.read()
      if ( v instanceof  FlaggedSystemData) {
        def  s = new SystemData ( a: v.a, b: v.b, c: v.c)
        outChannel.write(s)
        gatheredData.write(v)        
      }
      else {
        outChannel.write(v)        
      }  // end else
    }  // end while  
  }  // end run
}
