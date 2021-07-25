package examples.c14

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import jcsp.awt.*
import groovy_jcsp.*

class TargetController implements CSProcess {
	
  ChannelOutput getActiveTargets
  ChannelInput activatedTargets
  ChannelInput receivePoint
  ChannelOutputList sendPoint
  
  def Barrier setUpBarrier
  def Barrier goBarrier
  def AltingBarrier timeAndHitBarrier  
  int targets = 16

   void run() {
     def POINT = 1
     def BARRIER = 0
     def controllerAlt = new ALT ( [ timeAndHitBarrier, receivePoint] )
    
     setUpBarrier.sync()
     while (true) {
       getActiveTargets.write(1)
       def activeTargets = activatedTargets.read()  // a non-zero list of activated targets
       def runningTargets = activeTargets.size      // greater than zero
       ChannelOutputList sendList = []
       for ( t in activeTargets) sendList.append(sendPoint[t])
       def active = true
       goBarrier.sync()  
       while (active) {
         switch ( controllerAlt.priSelect() ) {
           case BARRIER:
             active = false
             break
           case POINT:
             def point = receivePoint.read()
             sendList.broadcast(point)
             break
         } // end switch
       } // end while active
     } // end while true
  } // end run
}
