package examples.c12.canteen

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class PhilosopherBehaviour implements CSProcess {
  
  int id  = -1
  ChannelOutput service
  ChannelInput deliver
  ChannelOutput toConsole
    
  void run() {
    def tim = new CSTimer()
    toConsole.write( "Starting ... \n")
    while (true) {
      toConsole.write( "Thinking ... \n")
      if (id > 0) {
        tim.sleep (3000)  
      }
      else {
        // Philosopher 0, has a 0.1 second think
        tim.sleep (100)
      }
      toConsole.write( "Need a chicken ...\n")
      service.write(id)
      def gotOne = deliver.read()
      if ( gotOne > 0 ) {
        toConsole.write( "Eating ... \n")
        tim.sleep (2000)  
        toConsole.write( "Brrrp ... \n")
      }
      else {
        toConsole.write( "                   Oh dear No chickens left \n")
      }  
    }
  }  
}
