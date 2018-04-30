package examples.c09
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*
import jcsp.userIO.Ask

def sources = Ask.Int ("Number of event sources between 1 and 9 ? ", 1, 9)

minTimes = [ 10, 20, 30, 40, 50, 10, 20, 30, 40 ]
maxTimes = [ 100, 150, 200, 50, 60, 30, 60, 100, 80 ]  
      
def es2ep = Channel.one2oneArray(sources)

ChannelInputList eventsList = new ChannelInputList (es2ep)

def sourcesList = ( 0 ..< sources).collect { i ->
            new EventSource ( source: i+1, 
                              outChannel: es2ep[i].out(),
                              minTime: minTimes[i],
                              maxTime: maxTimes[i] ) 
            }

def eventProcess = new EventProcessing ( eventStreams: eventsList,
                                          minTime: 10,
                                          maxTime: 400 )

new PAR( sourcesList + eventProcess).run()
