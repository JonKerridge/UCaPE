package examples.c03

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.plugAndPlay.*
import jcsp.lang.*
import groovyJCSP.*


def N2I = Channel.one2one()
def I2P = Channel.one2one()

def testList = [ new GNumbers ( outChannel: N2I.out() ),
                 new GIntegrate ( inChannel: N2I.in(), 
                                  outChannel: I2P.out() ),
                 new GPrint ( inChannel: I2P.in(), 
                              heading: "Integrate")
               ]

new PAR ( testList ).run()   
                                                           