package examples.c03

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.plugAndPlay.*
import jcsp.lang.*
import groovyJCSP.*

def N2P = Channel.one2one()

def testList = [ new GNumbers ( outChannel: N2P.out() ),
                 new GPrint ( inChannel: N2P.in(),
                              heading : "Numbers" )
               ]
new PAR ( testList ).run()
                                               