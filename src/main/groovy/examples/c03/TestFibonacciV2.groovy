package examples.c03

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovy_jcsp.plugAndPlay.*
import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.examples.*

def F2P = Channel.one2one()

def testList = [ new FibonacciV2 ( outChannel: F2P.out() ),
                 new GPrint ( inChannel: F2P.in(), 
                              heading: "Fibonacci V2" )
               ]

new PAR ( testList ).run()                          