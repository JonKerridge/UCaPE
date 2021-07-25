package examples.c03

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovy_jcsp.plugAndPlay.*
import jcsp.lang.*
import groovy_jcsp.*


def N2I = Channel.one2one()
def I2P = Channel.one2one()

def testList = [ new GNumbers ( outChannel: N2I.out() ),
                 new GIntegrate ( inChannel: N2I.in(), 
                                  outChannel: I2P.out() ),
                 new GPrint ( inChannel: I2P.in(), 
                              heading: "Integrate")
               ]

new PAR ( testList ).run()   
                                                           