package examples.c05

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com



import jcsp.lang.*

class QProducer implements CSProcess { 
	 
  def ChannelOutput put
  def int iterations = 100
  def delay = 0  
  
  void run () {
	def timer = new CSTimer()
    println  "QProducer has started"
	
    for ( i in 1 .. iterations ) {
      put.write(i)
      timer.sleep (delay)
    }
	put.write(null)
  }
}
