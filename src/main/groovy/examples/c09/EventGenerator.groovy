package examples.c09
  
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class EventGenerator implements CSProcess { 
	 
  ChannelOutput outChannel
  int source = 0
  int initialValue = 0
  int minTime = 100
  int maxTime = 1000
  int iterations = 10 
   
  void run () {    
    def es2udd = Channel.one2one()    
    println "Event Generator for source $source has started"  
	  
    def eventGeneratorList = [ 
            new EventStream ( source: source, 
                              initialValue: initialValue, 
                              iterations: iterations, 
                              outChannel: es2udd.out() ),
            new UniformlyDistributedDelay ( minTime: minTime, 
                                            maxTime: maxTime, 
                                            inChannel: es2udd.in(), 
                                            outChannel: outChannel )
            ]
	
    new PAR (eventGeneratorList).run()
  }
}
    