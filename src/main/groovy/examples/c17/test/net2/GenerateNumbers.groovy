package examples.c17.test.net2

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*


class GenerateNumbers implements CSProcess{
	
  def delay = 1000
  def iterations = 20
  def ChannelOutput outChannel  
  def generatedList = []
  
  void run() {
    println "Numbers started"
    def timer = new CSTimer()
    for (i in 1 .. iterations) {
      //println "Generated ${i}"
      outChannel.write(i)
      generatedList << i
      timer.sleep(delay)
    }
    println "Numbers finished"
    //println"Generated: ${generatedList}"
  }
}
