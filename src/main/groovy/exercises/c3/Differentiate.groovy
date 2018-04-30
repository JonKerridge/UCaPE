package exercises.c3

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*


class Differentiate implements CSProcess {
  
  def ChannelInput  inChannel
  def ChannelOutput outChannel
  
  void run() {
     
    One2OneChannel a = Channel.createOne2One()
    One2OneChannel b = Channel.createOne2One()
    One2OneChannel c = Channel.createOne2One()
    
    def differentiateList = [ new GPrefix ( prefixValue: 0, 
    		                                inChannel: b.in(), 
    		                                outChannel: c.out() ),
                              new GPCopy ( inChannel: inChannel,  
                            		       outChannel0: a.out(), 
                            		       outChannel1: b.out() ),
                              // insert a constructor for Minus                          
 							]
    
    new PAR ( differentiateList ).run()
    
  }
}
