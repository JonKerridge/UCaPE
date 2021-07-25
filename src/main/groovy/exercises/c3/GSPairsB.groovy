package exercises.c3

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*

class GSPairsB implements CSProcess {
  
  ChannelOutput outChannel
  ChannelInput  inChannel
  
  void run() {
    
    One2OneChannel a = Channel.one2one()
    One2OneChannel b = Channel.one2one()
    One2OneChannel c = Channel.one2one()
    
    def pairsList  = [ new GPlus   ( outChannel: outChannel, 
                                     inChannel0: a.in(),
                                     inChannel1: c.in() ),
                       new GSCopy  ( inChannel: inChannel, 
                                     outChannel0: b.out(), 
                                     outChannel1: a.out() ),
                       new GTail   ( inChannel: b.in(), 
                                     outChannel: c.out() ) 
                       ]
    new PAR ( pairsList ).run()
  }
}
