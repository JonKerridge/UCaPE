package examples.c06

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com



import jcsp.lang.*

class ConsumeHelloForTest implements CSProcess {  
	
  def ChannelInput inChannel
  def message  
  
  void run() {
    def first = inChannel.read()
    def second = inChannel.read()    
    message = "${first} ${second}!!!"    
    println message
  }
}

