package examples.c03

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.plugAndPlay.* 
import jcsp.lang.*
import groovyJCSP.*

def connect = Channel.one2oneArray(5)
def outChans = Channel.one2oneArray(3)

def printList = new ChannelInputList ( outChans )

def titles = [ "n", "int", "sqr" ]

def testList =  [ new GNumbers ( outChannel: connect[0].out() ),
                  new GPCopy ( inChannel: connect[0].in(),
                               outChannel0: connect[1].out(),
                               outChannel1: outChans[0].out() ),
                  new GIntegrate ( inChannel: connect[1].in(), 
                                   outChannel: connect[2].out() ),
                  new GPCopy ( inChannel: connect[2].in(),
                               outChannel0: connect[3].out(),
                               outChannel1: outChans[1].out() ),
                  new GPairs ( inChannel: connect[3].in(), 
                               outChannel: connect[4].out() ),
                  new GPrefix ( prefixValue: 0, 
                                inChannel: connect[4].in(), 
                                outChannel: outChans[2].out() ),
                  new GParPrint ( inChannels: printList, 
                                  headings: titles )
                ]

new PAR ( testList ).run()  
                              