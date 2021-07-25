package examples.c06

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com



import jcsp.lang.*

class QConsumerForTest implements CSProcess {  
	
  ChannelOutput get
  ChannelInput receive
  def long delay = 0  
  def outSequence = []  
  
  void run () {
    def timer = new CSTimer()
    def running = true
	
    while (running) {
      get.write(1)  
      def v = receive.read()
      timer.sleep (delay)
      if ( v != null) outSequence = outSequence << v
      else running = false
    }
  }
}

