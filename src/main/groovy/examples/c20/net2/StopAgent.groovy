package examples.c20.net2

import jcsp.net2.*
import jcsp.lang.*
import groovyJCSP.*



class StopAgent implements MobileAgent {
  
  def ChannelOutput toLocal
  def ChannelInput fromLocal
  def int homeNode
  def int previousNode
  def boolean initialised
  def NetChannelLocation nextNodeInputEnd
                  
  def connect (c) {
    this.toLocal = c[0]
    this.fromLocal = c[1]

  }
  
  def disconnect () {
    this.toLocal = null
    this.fromLocal = null
  }

  void run() {
    println "SA: running $homeNode, $previousNode, $initialised"
    toLocal.write(homeNode)	// tells node not to send to this node
    toLocal.write(previousNode) // where we want to get to
    toLocal.write(initialised)
    if ( ! initialised) {
      nextNodeInputEnd = fromLocal.read()
      initialised = true
      println "SA: initialised"
    }
    def gotThere = fromLocal.read()
    if ( gotThere ) {
      toLocal.write(nextNodeInputEnd)
      println "SA: got to $previousNode"
    }
  } // end run
}
