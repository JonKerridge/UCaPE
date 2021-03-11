package exercises.c3

import groovy_jcsp.plugAndPlay.GPrint
import jcsp.lang.*
import groovy_jcsp.*

One2OneChannel S2P = Channel.createOne2One()

def testList = [ new GSquares ( outChannel: S2P.out() ),
                 new GPrint   ( inChannel: S2P.in(),
                                heading : "Squares" )
               ]

new PAR ( testList ).run()       