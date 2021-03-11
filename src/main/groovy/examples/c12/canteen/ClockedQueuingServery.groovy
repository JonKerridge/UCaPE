package examples.c12.canteen

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*

class ClockedQueuingServery implements CSProcess{

  def ChannelInput service    
  def ChannelOutput deliver    
  def ChannelInput supply   
   
  void run() {
    
    def console = Channel.any2one()
    
    def clock = new Clock ( toConsole: console.out() )
    
    def servery = new QueuingCanteen ( service: service,
                                        deliver: deliver,
                                        supply: supply,
                                        toConsole: console.out() )
    def serveryConsole = new GConsole ( toConsole: console.in(),
                                        frameLabel: "Clocked Queuing Servery")
    new PAR([servery, serveryConsole, clock ]).run()
  }

}