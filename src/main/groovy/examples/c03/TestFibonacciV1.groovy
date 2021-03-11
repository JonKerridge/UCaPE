package examples.c03

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovy_jcsp.plugAndPlay.*
import groovy_jcsp.examples.*
import jcsp.lang.*
import groovy_jcsp.*

def F2P = Channel.one2one()

def testList = [ new FibonacciV1 ( outChannel: F2P.out() ),
                 new GPrint ( inChannel: F2P.in(), 
                              heading: "Fibonacci V1" )
               ]

new PAR ( testList ).run()  
                        