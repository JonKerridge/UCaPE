package exercises.c3

import groovyJCSP.plugAndPlay.GPrint
import jcsp.lang.*
import groovyJCSP.*

One2OneChannel S2P = Channel.createOne2One()

def testList = [ new GSquares ( outChannel: S2P.out() ),
                 new GPrint   ( inChannel: S2P.in(),
                                heading : "Squares" )
               ]

new PAR ( testList ).run()       