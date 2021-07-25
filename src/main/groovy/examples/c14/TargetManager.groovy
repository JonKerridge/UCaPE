package examples.c14

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class TargetManager implements CSProcess {
	
  ChannelInput targetIdFromTarget
  ChannelInput getActiveTargets
  ChannelOutput activatedTargets
  ChannelOutput activatedTargetsToDC
  ChannelInput targetsFlushed
  ChannelOutput flushNextBucket
  def Barrier setUpBarrier

   void run() {
    setUpBarrier.sync()
    while (true) {
      def targetList = [ ]
      getActiveTargets.read()
      flushNextBucket.write(1)
      def targetsRunning = targetsFlushed.read()  
      while (targetsRunning > 0) {
         targetList << targetIdFromTarget.read()
         targetsRunning = targetsRunning - 1
      } // end of while targetsRunning
      activatedTargets.write(targetList)
      activatedTargetsToDC.write(targetList)
    } // end of while true
  } 
}
