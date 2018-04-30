package examples.c03

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.plugAndPlay.*
import jcsp.lang.*
import groovyJCSP.*

def S2P = Channel.one2one()

def testList = [ new GSquares ( outChannel: S2P.out() ),
                 new GPrint ( inChannel: S2P.in(),
                              heading : "Squares" )
               ]

new PAR ( testList ).run()                                               