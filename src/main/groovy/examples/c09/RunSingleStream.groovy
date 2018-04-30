package examples.c09
  
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

def eg2h = Channel.one2one()
def h2udd = Channel.one2one()
def udd2prn = Channel.one2one()
def eventTestList = [ 
      new EventGenerator ( source: 1, 
                           initialValue: 100, 
                           iterations: 100, 
                           outChannel: eg2h.out(), 
                           minTime: 100, 
                           maxTime:200 ),
					   
      new EventHandler ( inChannel: eg2h.in(), 
                         outChannel: h2udd.out() ),
					 
      new UniformlyDistributedDelay ( inChannel:h2udd.in(), 
                                      outChannel: udd2prn.out(), 
                                      minTime: 1000, 
                                      maxTime: 2000 ), 
								  
      new GPrint ( inChannel: udd2prn.in(),
    		        heading : "Event Output",
    		        delay: 0)
      ]

new PAR ( eventTestList ).run() 
             