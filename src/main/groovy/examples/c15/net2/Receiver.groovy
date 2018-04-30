package examples.c15.net2

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*

class Receiver implements CSProcess {
  
  def ChannelInput inChannel
  
  void run() {
    while (true) {
      def v = inChannel.read()
      println "$v"
    }
  }
}

