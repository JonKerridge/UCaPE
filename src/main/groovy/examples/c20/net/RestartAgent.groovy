package examples.c20.net;

import jcsp.net.mobile.*
import jcsp.net.*
import jcsp.lang.*
import groovyJCSP.*

class RestartAgent implements MobileAgent {
  
  def ChannelOutput toLocal
  def ChannelInput fromLocal
  def int homeNode
  def int previousNode
  def boolean firstHop
                  
  def connect (c) {
    this.toLocal = c[0]
    this.fromLocal = c[1]

  }
  
  def disconnect () {
    this.toLocal = null
    this.fromLocal = null
  }

  void run() {    
    println "RA: running $homeNode, $previousNode"
    toLocal.write(firstHop)
    if (firstHop) { firstHop = false }
    toLocal.write(homeNode)	// tells node to resume sending to this node
    toLocal.write(previousNode)
    println "RA: finished"
  }

}