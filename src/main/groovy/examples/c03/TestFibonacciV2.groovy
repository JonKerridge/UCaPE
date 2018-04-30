package examples.c03

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.plugAndPlay.*
import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.examples.*

def F2P = Channel.one2one()

def testList = [ new FibonacciV2 ( outChannel: F2P.out() ),
                 new GPrint ( inChannel: F2P.in(), 
                              heading: "Fibonacci V2" )
               ]

new PAR ( testList ).run()                          