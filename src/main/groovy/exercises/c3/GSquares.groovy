package exercises.c3

import groovy_jcsp.plugAndPlay.*
import jcsp.lang.*
import groovy_jcsp.*

class GSquares implements CSProcess {
  
  ChannelOutput outChannel
  
  void run () {

    One2OneChannel N2I = Channel.one2one()
    One2OneChannel I2P = Channel.one2one()

    def testList =  [ new GNumbers   ( outChannel: N2I.out() ),
                      new GIntegrate ( inChannel: N2I.in(), 
                                       outChannel: I2P.out() ),
                      // you will need to modify thi twice
                      //first modification is to insert a constructor for GSPairsA
                      // then run the network using TestGSCopy
                      //second modification replace the constructor for GSPairsA with GSPairsB
                      // then run the network again using TestGSCopy
                      // you will then be able to compare the behaviour and also to
                      // explain why this happens!
                    ]
    new PAR ( testList ).run()  
  }
}
                              