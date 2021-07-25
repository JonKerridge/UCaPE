package exercises.c3

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*

class DifferentiateNeg implements CSProcess {
  
  ChannelInput  inChannel
  ChannelOutput outChannel
  
  void run() {
    
    One2OneChannel a = Channel.one2one()
    One2OneChannel b = Channel.one2one()
    One2OneChannel c = Channel.one2one()
    One2OneChannel d = Channel.one2one()
    
    def differentiateList = [ new GPrefix ( prefixValue: 0, 
                                            inChannel: b.in(), 
                                            outChannel: c.out() ),
                              new GPCopy ( inChannel: inChannel,  
                            		       outChannel0: a.out(), 
                            		       outChannel1: b.out() ),
                              //insert a constructor for Negator
                              new GPlus  ( inChannel0: a.in(), 
                            		       inChannel1: d.in(), 
                            		       outChannel: outChannel ) 
                            ]
    
    new PAR ( differentiateList ).run()
    
  }
}
