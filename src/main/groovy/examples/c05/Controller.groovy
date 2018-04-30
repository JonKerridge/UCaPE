package examples.c05
      
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*
 
class Controller implements CSProcess {
	
  def long testInterval = 11000
  def long computeInterval = 2000
  def int addition = 1
  
  def ChannelInput factor
  def ChannelOutput suspend
  def ChannelOutput injector
  
  void run() {
    def currentFactor = 0
    def timer = new CSTimer()
    def timeout = timer.read()                   // get current time
	
    while (true) {
      timeout = timeout + testInterval           // set the timeout
      timer.after ( timeout )                    // wait for the timeout
      suspend.write (0)                          // suspend signal to ScaleInt; value irrelevant
      currentFactor = factor.read()              // get current scaling from ScaleInt
      currentFactor = currentFactor + addition   // compute new factor
      timer.sleep(computeInterval)               // to simulate computational time
      injector.write ( currentFactor )          // send new scale factor to Scale
    }
  }
}
