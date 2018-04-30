package examples.c05

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com



import jcsp.lang.*

class QConsumer implements CSProcess {  
	
  def ChannelOutput get
  def ChannelInput receive  
  def long delay = 0 
   
  void run () {
    def timer = new CSTimer()
    println  "QConsumer has started"
    def running = true
	
    while (running) {
      get.write(1)  
      def v = receive.read()
      println "QConsumer has read $v"
      timer.sleep (delay)
      if ( v == null ) running = false
    }
  }
}

