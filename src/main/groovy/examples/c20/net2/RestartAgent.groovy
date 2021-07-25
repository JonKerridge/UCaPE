package examples.c20.net2

import jcsp.lang.*
import groovy_jcsp.*



class RestartAgent implements MobileAgent {
  
  ChannelOutput toLocal
  ChannelInput fromLocal
  int homeNode
  int previousNode
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
  } // end run
}
