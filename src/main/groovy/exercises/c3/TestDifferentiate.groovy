package exercises.c3

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*

One2OneChannel N2I = Channel.one2one()
One2OneChannel I2D = Channel.one2one()
One2OneChannel D2P = Channel.one2one()

def testList = [ new GNumbers ( outChannel: N2I.out() ),
                 new GIntegrate ( inChannel: N2I.in(), 
                		          outChannel: I2D.out() ),
                 new Differentiate ( inChannel:I2D.in(), 
                		             outChannel:D2P.out() ),
                 new GPrint ( inChannel: D2P.in(), 
                              heading: "Differentiated Numbers" )
               ]

new PAR ( testList ).run()                          