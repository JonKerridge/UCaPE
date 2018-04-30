package examples.c10

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

class Elementv0 implements CSProcess {
	
  def ChannelInput fromRing
  def ChannelOutput toRing
  def int element
  def int nodes
  def int iterations = 12
  
  void run() {
    def S2RE = Channel.one2one()
    def RE2R = Channel.one2one()
    def R2GEC = Channel.one2one()
    
    def nodeList = [ new Sender ( toElement: S2RE.out(), 
                                   element: element, 
                                   nodes: nodes,
                                   iterations: iterations),
                     new Receiver ( fromElement: RE2R.in(), 
                                    outChannel: R2GEC.out(),
                                     element: element),
                     new RingElementv0 ( fromLocal: S2RE.in(), 
                                         toLocal: RE2R.out(), 
                                         fromRing: fromRing, 
                                         toRing: toRing, 
                                         element: element),
                     new GConsole ( toConsole: R2GEC.in(),
                                           frameLabel: "Element: " + element)
                   ]
    new PAR ( nodeList ).run()
  }
}

    
  