package examples.c09;
 
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class EventHandler implements CSProcess { 
	 
  ChannelInput inChannel
  ChannelOutput outChannel  
  
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
