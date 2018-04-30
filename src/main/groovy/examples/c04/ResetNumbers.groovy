package examples.c04

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

class ResetNumbers implements CSProcess {
  
  def ChannelOutput outChannel
  def ChannelInput resetChannel
  def int initialValue = 0
  
  void run() {
    
    def a = Channel.one2one ()
    def b = Channel.one2one()
    def c = Channel.one2one()
    
    def testList = [ new ResetPrefix ( prefixValue: initialValue, 
                                       outChannel: a.out(), 
                                       inChannel: c.in(),
                                       resetChannel: resetChannel ),
                     new GPCopy ( inChannel: a.in(), 
                            	  outChannel0: outChannel, 
                            	  outChannel1: b.out() ),
                     new GSuccessor ( inChannel: b.in(), 
                            		  outChannel: c.out()) 
                   ]
    new PAR ( testList ).run()    
  }
}
                         
