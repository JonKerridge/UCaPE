package sanityCheck

import jcsp.lang.CSProcess
import jcsp.lang.ChannelOutput

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.userIO.Ask

class Producer implements CSProcess {
  
  def ChannelOutput outChannel
  def produceList = []
  void run() {
    def i = 1
    println "Producing integers in the range 1 to 10"
    while ( i < 11 ) {
      outChannel.write (i)
      produceList << i
      println "written $i to Consumer process"
      i += 1
    }
    outChannel.write(-1)
    println "Producer has terminated"
  }
}
