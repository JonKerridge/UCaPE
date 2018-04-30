package examples.c02

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.userIO.*
import jcsp.lang.*

class ProduceHN implements CSProcess {
  
  def ChannelOutput outChannel
  
  void run() {
    def hi = "Hello"
    def thing = Ask.string ("\nName ? ")
    outChannel.write ( hi )
    outChannel.write ( thing )   
  } 
}

      
  
  
