package examples.c12.canteen;

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*

class Kitchen implements CSProcess{
  
  ChannelOutput supply

  void run() {
    
    def console = Channel.one2one()     
    def chef = new Chef ( supply: supply,
                          toConsole: console.out() )
    def chefConsole = new GConsole ( toConsole: console.in(),
                                     frameLabel: "Chef")
    new PAR([chef, chefConsole]).run()
  }
}