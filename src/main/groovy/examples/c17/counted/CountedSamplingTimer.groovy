package examples.c17.counted

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.* 
import jcsp.lang.*


class CountedSamplingTimer implements CSProcess {
	
  def ChannelOutput sampleRequest
  def sampleInterval
  def ChannelInput countReturn
  def ChannelOutput countToGatherer 
   
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
