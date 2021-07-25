package examples.c15.net2

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*

class Put implements CSProcess {
	 
  ChannelOutput outChannel  
  
  void run() {
    int i = 1
    while (true) {
      outChannel.write ( i )
      i = i + 1
    }
  }
}

      
  
