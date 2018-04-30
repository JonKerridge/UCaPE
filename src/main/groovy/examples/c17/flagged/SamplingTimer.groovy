package examples.c17.flagged

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.*
import jcsp.lang.*


class SamplingTimer implements CSProcess {
	
  def ChannelOutput sampleRequest
  def sampleInterval 
   
  void run() {
    def timer = new CSTimer()
    while (true){
      timer.sleep(sampleInterval)
      sampleRequest.write(1)
    }
  }
}
