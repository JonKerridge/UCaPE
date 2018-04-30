package examples.c17.flagged

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.*
import jcsp.lang.*


class Sampler implements CSProcess {  
	
  def ChannelInput inChannel
  def ChannelOutput outChannel
  def ChannelInput sampleRequest
  
  void run() {
    def sampleAlt = new ALT ([sampleRequest, inChannel])
	
    while (true){
      def index = sampleAlt.priSelect()
      if (index == 0) {
        sampleRequest.read()
        def v = inChannel.read()
        def fv = new FlaggedSystemData ( a: v.a, b:v.b, testFlag: true)
        outChannel.write(fv)
      }
      else {
        outChannel.write(inChannel.read())
      } // end else
    } // end while
  } // end run
}
