package exercises.c4

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*


class ResetNumbers implements CSProcess {
  
  ChannelOutput outChannel
  ChannelInput resetChannel
  int initialValue = 0
  
  void run() {
    
    One2OneChannel a = Channel.one2one()
    One2OneChannel b = Channel.one2one()
    One2OneChannel c = Channel.one2one()
    
    def testList = [ new GPrefix ( prefixValue: initialValue, 
                                   outChannel: a.out(), 
                                   inChannel: c.in() ),
                     new GPCopy ( inChannel: a.in(), 
                            	  outChannel0: outChannel, 
                            	  outChannel1: b.out() ),
                     // requires a constructor for ResetSuccessor
                  ]
    new PAR ( testList ).run()    
  }
}
                         
