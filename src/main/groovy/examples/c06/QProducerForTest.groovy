package examples.c06

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com



import jcsp.lang.*

class QProducerForTest implements CSProcess {  
	
  def ChannelOutput put
  def int iterations = 100
  def delay = 0  
  def sequence = [] 
   
  void run () {
	def timer = new CSTimer()
	
    for ( i in 1 .. iterations ) {
      put.write(i)
      timer.sleep (delay)
      sequence = sequence << i
    }
	put.write(null)
  }
}
