package examples.c12.canteen;

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*

class InstantServery implements CSProcess{

  ChannelInput service
  ChannelOutput deliver
  ChannelInput supply
    
  void run() {
      
    def console = Channel.one2one()
      
    def servery = new InstantCanteen ( service: service,
                                        deliver: deliver,
                                        supply: supply,
                                        toConsole: console.out() )
    def serveryConsole = new GConsole ( toConsole: console.in(),
                                        frameLabel: "Instant Servery")
    new PAR([servery,serveryConsole]).run()
  }

}