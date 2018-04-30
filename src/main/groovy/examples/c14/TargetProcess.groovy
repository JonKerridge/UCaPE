package examples.c14
  
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import java.awt.Point
import groovyJCSP.*
 
class TargetProcess implements CSProcess {
	
  def ChannelOutput targetRunning
  def ChannelOutput stateToDC
  def ChannelInput mousePoint
  def Barrier setUpBarrier
  def Barrier initBarrier
  def Barrier goBarrier
  def AltingBarrier timeAndHitBarrier
  def buckets
  def int targetId
  def int x
  def int y
  def delay = 2000
  
  def boolean within ( Point p, int x, int y) {
    def maxX = x + 100
    def maxY = y + 100
    if ( p.x < x ) return false
    if ( p.y < y ) return false
    if ( p.x > maxX ) return false
    if ( p.y > maxY ) return false
    return true
  }
  
  void run() {
    def rng = new Random()
    def timer = new CSTimer()
    def int range = buckets.size() / 2
    def bucketId = rng.nextInt( range )
    def POINT= 1
    def TIMER = 0
    def BARRIER = 0    
    def TIMED_OUT = 0
    def HIT = 1

    def preTimeOutAlt = new ALT ([ timer, mousePoint ])
    def postTimeOutAlt = new ALT ([ timeAndHitBarrier, mousePoint ])
    
    timeAndHitBarrier.resign()
    setUpBarrier.sync()
    buckets[bucketId].fallInto()
    while (true) {
      timeAndHitBarrier.enroll()
      goBarrier.enroll()
      targetRunning.write(targetId)
      initBarrier.sync()    //ensures all targets have initialised
      def running = true
      def resultList = [targetId]
      goBarrier.resign()
      timer.setAlarm( timer.read() + delay + rng.nextInt(delay) )
      while ( running ) {
        switch (preTimeOutAlt.priSelect() ) {
          case TIMER:
            running = false
            resultList << TIMED_OUT
            stateToDC.write(resultList)
            break
          case POINT:
            def point = mousePoint.read()
            if ( within(point, x, y) ) {
              running = false
              resultList << HIT
              stateToDC.write(resultList)
            }
            else {              
            }
            break
        }
      } // end while running
      def awaiting = true
      while (awaiting) {
        switch (postTimeOutAlt.priSelect() ) {
          case BARRIER:
            awaiting = false
            timeAndHitBarrier.resign()
            break
          case POINT:
            mousePoint.read()
            break
        }
      } // end while awaiting
      bucketId = ( bucketId + 2 + rng.nextInt(8) ) % buckets.size()
      buckets[bucketId].fallInto()
    }// end while true
  }
}
      
      
      
      
      
      
