package examples.c12.canteen;

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

class Philosopher implements CSProcess{

  def ChannelOutput service
  def ChannelInput deliver
  def int philosopherId

  void run() {
    
    def console = Channel.one2one()    
    def philosopher = new PhilosopherBehaviour ( service: service,
                                                  deliver: deliver,
                                                  toConsole: console.out(),
                                                  id: philosopherId)
    def philosopherConsole = new GConsole ( toConsole: console.in(),
                                            frameLabel: "Philosopher $philosopherId")
    new PAR( [ philosopher, philosopherConsole]).run()
  }
}
