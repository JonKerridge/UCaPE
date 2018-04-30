package examples.c09;
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class EventHandler implements CSProcess { 
	 
  def ChannelInput inChannel
  def ChannelOutput outChannel  
  
  void run () {    
    def get = Channel.one2one()
    def transfer = Channel.one2one()
    def toBuffer = Channel.one2one() 
	   
    def handlerList = [ new EventReceiver ( eventIn: inChannel, 
                                            eventOut: toBuffer.out()),
                        new EventOWBuffer ( inChannel: toBuffer.in(), 
                                            getChannel: get.in(),
                                            outChannel: transfer.out() ),
                        new EventPrompter ( inChannel: transfer.in(), 
                                            getChannel: get.out(),
                                            outChannel: outChannel )
                      ]
    new PAR ( handlerList ).run()
  }
}
