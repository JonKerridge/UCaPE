package examples.c17.counted

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovy_jcsp.*
import jcsp.lang.*


class CountedSamplingTimer implements CSProcess {
	
  ChannelOutput sampleRequest
  def sampleInterval
  ChannelInput countReturn
  ChannelOutput countToGatherer
   
  void run() {
    def timer = new CSTimer()
    while (true){
      timer.sleep(sampleInterval)
      sampleRequest.write(1)
      def c= countReturn.read()
      countToGatherer.write(c)
    }
  }
}
