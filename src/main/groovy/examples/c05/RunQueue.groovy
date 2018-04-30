package examples.c05
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

def QP2Q = Channel.one2one()
def Q2QC = Channel.one2one()
def QC2Q = Channel.one2one()

def testList = [ new QProducer ( put: QP2Q.out(), 
		                         iterations: 50,
		                         delay: 0),
                 new Queue ( put: QP2Q.in(),
                		     get: QC2Q.in(), 
                		     receive: Q2QC.out(), 
                		     elements: 5 ),
                 new QConsumer ( get: QC2Q.out(), 
                		         receive: Q2QC.in(), 
                		         delay: 0 )
               ]

new PAR ( testList ).run()
