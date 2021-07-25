package examples.c17.counted

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovy_jcsp.*
import jcsp.lang.*


class CountingSampler implements CSProcess {  
	
  ChannelInput inChannel
  ChannelOutput outChannel
  ChannelInput sampleRequest
  ChannelOutput countReturn
  
  void run() {
    def sampleAlt = new ALT ([sampleRequest, inChannel])
    def counter = 0
	
    while (true){
      counter = counter + 1
      def index = sampleAlt.priSelect()
      if (index == 0) {
        sampleRequest.read()
        def v = inChannel.read()
        outChannel.write(v)
        println "Sampled Value was ${v}"
        countReturn.write(counter)
      }
      else {
        outChannel.write(inChannel.read())
      } // end else
    } // end while
  } // end run
}
