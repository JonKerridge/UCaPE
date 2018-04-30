package examples.c12.canteen
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*


class Chef implements CSProcess {
  
  def ChannelOutput supply
  def ChannelOutput toConsole
  
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
