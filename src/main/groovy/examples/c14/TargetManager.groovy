package examples.c14

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class TargetManager implements CSProcess {
	
  def ChannelInput targetIdFromTarget
  def ChannelInput getActiveTargets
  def ChannelOutput activatedTargets
  def ChannelOutput activatedTargetsToDC
  def ChannelInput targetsFlushed
  def ChannelOutput flushNextBucket
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
