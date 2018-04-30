package examples.c15.net2

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*

class Get implements CSProcess {
	 
  def ChannelInput inChannel
  def int id = 0  
  
  void run() {
    def timer = new CSTimer()
    while (true) {
      def v = inChannel.read()
      println "$id .. $v"
      timer.sleep(200 * v)
    }
  }
}

