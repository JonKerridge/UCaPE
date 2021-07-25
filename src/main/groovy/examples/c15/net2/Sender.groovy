package examples.c15.net2

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*

class Sender implements CSProcess {
  
  ChannelOutput outChannel
  def String id
  
  void run() {
    def timer = new CSTimer()
    while (true) {
      timer.sleep(10000)
      outChannel.write ( id )
    }
  }
}

      
  
