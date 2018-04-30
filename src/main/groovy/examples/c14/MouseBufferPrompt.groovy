package examples.c14

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class MouseBufferPrompt implements CSProcess{
	
  def ChannelOutput returnPoint
  def ChannelOutput getPoint
  def ChannelInput receivePoint  
  def Barrier setUpBarrier

  void run () {
    setUpBarrier.sync()
    while (true) {
      getPoint.write( 1 )
      def point = receivePoint.read()
      returnPoint.write( point )
    }    
  }
}
