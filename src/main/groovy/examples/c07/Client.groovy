package examples.c07

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*


class Client implements CSProcess{  
	
  def ChannelInput receiveChannel
  def ChannelOutput requestChannel
  def clientNumber   
  def selectList = [ ] 
   
  void run () {
    def iterations = selectList.size
    println "Client $clientNumber has $iterations values in $selectList"
	
    for ( i in 0 ..< iterations) {
      def key = selectList[i]
      requestChannel.write(key)
      def v = receiveChannel.read()
    }
	
    println "Client $clientNumber has finished"
  }
}
