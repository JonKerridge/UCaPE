package examples.c21.net2

import jcsp.net2.*
import jcsp.lang.*
import groovyJCSP.*
 



class AdaptiveAgent implements MobileAgent, Serializable {
  
  def ChannelInput fromInitialNode
  def ChannelInput fromVisitedNode
  def ChannelOutput toVisitedNode
  def ChannelOutput toReturnedNode
  
  def initial = true
  def visiting = false
  def returned = false
  
  def availableNodes = [ ]
  def requiredProcess = null
  def returnLocation
  def processDefinition = null
  def homeNode
  
  def connect ( c) {
    if (initial) {
      fromInitialNode = c[0]
      returnLocation = c[1]
      homeNode = c[2]                   
    }
    if (visiting) {
      fromVisitedNode = c[0]
      toVisitedNode = c[1]
    }
    if (returned) {
      toReturnedNode = c[0]
    }
  }
  
  def disconnect() {
    fromInitialNode = null
    fromVisitedNode = null
    toVisitedNode = null
    toReturnedNode = null
  }

  void run( ) {
    if (returned) {
      toReturnedNode.write([processDefinition, requiredProcess])
      println "AA: returned agent has written $requiredProcess to home node"
    }
    
    if (visiting) {
      toVisitedNode.write(requiredProcess)
      println "AA: visitor wants $requiredProcess"
      processDefinition = fromVisitedNode.read()
      println "AA: visitor received $processDefinition"
      if ( processDefinition != null ) {
        toVisitedNode.write(homeNode)
        visiting = false
        returned = true
        def nextNodeLocation = returnLocation
        def nextNodeChannel = NetChannel.any2net(nextNodeLocation)
        println "AA: visitor being sent home to $nextNodeLocation"
        disconnect()
        nextNodeChannel.write(this)  // THIS has become NOT serializable!!
        println "AA: visitor is returning home"
      } 
      else { //determine next node to visit and go there
        disconnect()          
        def nextNodeLocation = availableNodes.pop()
        def nextNodeChannel = NetChannel.any2net(nextNodeLocation)
        println "AA: visitor continuing journey to $nextNodeLocation"
        nextNodeChannel.write(this)        
        println "AA: visitor has continued journey"
      }
    }
    
    if (initial) {
      def awaitingTypeName = true
      while (awaitingTypeName) {
        def d = fromInitialNode.read()
        if ( d instanceof List) {
          for ( i in 0 ..< d.size) { availableNodes << d[i] }
        }
        if ( d instanceof String) {
          requiredProcess = d
          awaitingTypeName = false
          initial = false
          visiting = true
          disconnect()          
          //determine next node to visit and go there
          def nextNodeLocation = availableNodes.pop()
          def nextNodeChannel = NetChannel.any2net(nextNodeLocation)
          println "AA: initial going visiting to $nextNodeLocation"
          nextNodeChannel.write(this)
          println "AA: initial has been sent to another node"
        }
      }
    }
    
  } // end run
}
