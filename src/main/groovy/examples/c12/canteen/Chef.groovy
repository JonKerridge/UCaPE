package examples.c12.canteen
 
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovy_jcsp.*


class Chef implements CSProcess {
  
  ChannelOutput supply
  ChannelOutput toConsole
  
  void run () {

    def tim = new CSTimer()   
    def CHICKENS = 4
      
    toConsole.write( "Starting ... \n")
    while(true){      
      toConsole.write( "Cooking ... \n")    // cook 4 chickens
      tim.after (tim.read () + 2000)        // this takes 2 seconds to cook
      toConsole.write( "$CHICKENS chickens ready ... \n")
      supply.write (CHICKENS)            
      toConsole.write( "Taking chickens to Canteen ... \n")
      supply.write (0)                     
    }
  }  
}
