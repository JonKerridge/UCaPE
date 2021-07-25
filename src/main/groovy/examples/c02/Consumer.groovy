package examples.c02
    
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com



import jcsp.lang.*
    
class Consumer implements CSProcess {
  
  ChannelInput inChannel
  
  void run() {
    int i = 1000
    while ( i > 0 ) {
      i = inChannel.read()
      println "the input was : ${i}"
    }
    println "Finished"
  }
}

