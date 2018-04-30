package examples.c12.canteen;

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

class Kitchen implements CSProcess{
  
  def ChannelOutput supply

  void run() {
    
    def console = Channel.one2one()     
    def chef = new Chef ( supply: supply,
                          toConsole: console.out() )
    def chefConsole = new GConsole ( toConsole: console.in(),
                                     frameLabel: "Chef")
    new PAR([chef, chefConsole]).run()
  }
}