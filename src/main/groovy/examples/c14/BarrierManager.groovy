package examples.c14

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*


class BarrierManager implements CSProcess{
	
  def AltingBarrier timeAndHitBarrier
  def AltingBarrier finalBarrier
  def Barrier goBarrier
  def Barrier setUpBarrier

  void run() {
    def timeHitAlt = new ALT ([timeAndHitBarrier])
    def finalAlt = new ALT ([finalBarrier])
    setUpBarrier.sync()
	
    while (true){
      goBarrier.sync()
      def t = timeHitAlt.select()
      def f = finalAlt.select()
    }
  }
}
