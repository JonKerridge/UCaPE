package examples.c08;

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.*
import jcsp.lang.*


class Server implements CSProcess{
	  
  def ChannelInputList fromMux
  def ChannelOutputList toMux  
  def dataMap = [ : ] 
                   
  void run() {
    def serverAlt = new ALT(fromMux)
	
    while (true) {
      def index = serverAlt.select()
      def key = fromMux[index].read()
      toMux[index].write(dataMap[key])
    }    
  }
}
