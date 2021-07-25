package examples.c02
        
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.userIO.*
import jcsp.lang.*
  
class Producer implements CSProcess {
  
  ChannelOutput outChannel
  
  void run() {
    int i = 1000
    while ( i > 0 ) {
      i = Ask.Int ("next: ", -100, 100)
      outChannel.write (i)
    }
  }
}
