package examples.c03

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovy_jcsp.plugAndPlay.*
import jcsp.lang.*
import groovy_jcsp.*

def N2P = Channel.one2one()

def testList = [ new GNumbers ( outChannel: N2P.out() ),
                 new GPrint ( inChannel: N2P.in(),
                              heading : "Numbers" )
               ]
new PAR ( testList ).run()
                                               