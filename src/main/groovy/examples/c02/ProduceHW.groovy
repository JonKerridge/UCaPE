package examples.c02

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com



import jcsp.lang.*

class ProduceHW implements CSProcess {
  
  ChannelOutput outChannel
  
  void run() {
    def hi = "Hello"
    def thing = "World"
    outChannel.write ( hi )
    outChannel.write ( thing )   
  } 
}

      
  
  
