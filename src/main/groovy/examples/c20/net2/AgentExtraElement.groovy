package examples.c20.net2
 
import jcsp.lang.*
import jcsp.net2.*
 



class AgentExtraElement implements CSProcess { 
	
  def ChannelInput fromRing
  def ChannelOutput toRing
  
  def void run () {
    def  N2A = Channel.one2one()
    def  A2N = Channel.one2one()  

    def ChannelInput toAgentInEnd = N2A.in()
    def ChannelInput fromAgentInEnd = A2N.in()
    def ChannelOutput toAgentOutEnd = N2A.out()
    def ChannelOutput fromAgentOutEnd = A2N.out()
    
    println "Extra Element starting ..."
    def NetChannelLocation originalToRing = toRing.getLocation()
    def emptyPacket = new RingPacket ( source: -1, destination: -1 , value: -1 , full: false)
    def element = 1 	// by default

    while (true) {
      def ringBuffer = fromRing.read()
      if ( ringBuffer instanceof RingPacket) {
        toRing.write( ringBuffer ) 
      }
      else {
        if (ringBuffer instanceof StopAgent) {
          def theAgent = ringBuffer
          theAgent.connect ( [fromAgentOutEnd, toAgentInEnd] )
          def agentManager = new ProcessManager (theAgent)
          agentManager.start()
          def failedNode = fromAgentInEnd.read()
          def targetNode = fromAgentInEnd.read()
          def alreadyInitialised = fromAgentInEnd.read()
          if ( ! alreadyInitialised ) {
            toAgentOutEnd.write (fromRing.getLocation())
          }
          if (element == targetNode) {
            // got to node that needs to be changed
            toAgentOutEnd.write(true)
            def NetChannelLocation revisedToRing = fromAgentInEnd.read()
            toRing = NetChannel.any2net(revisedToRing)
            agentManager.join()
            theAgent.disconnect()
            println "Node $element: stopping has redirected toRing"
            // no need to send agent any further its got to its target
            // ring has lost a node hence do not send an empty packet
          }
          else {
            toAgentOutEnd.write(false)
            agentManager.join()
            theAgent.disconnect()
            toRing.write(theAgent)
            println "Node $element: stopping has passed agent on to next node"
          }         
        }
        else {
          // must be instance of RestartAgent
          def theAgent = ringBuffer
          theAgent.connect ( [fromAgentOutEnd, toAgentInEnd] )
          def agentManager = new ProcessManager (theAgent)
          agentManager.start()
          def firstHop = fromAgentInEnd.read()
          def resumedNode = fromAgentInEnd.read()
          def targetNode = fromAgentInEnd.read()
          if (firstHop) {
            agentManager.join()
            theAgent.disconnect()
            toRing.write(theAgent)
           }
          else {
            if (element == targetNode) {                
              toRing = NetChannel.any2net (originalToRing)
              println "Node $element: restarting has redirected toRing"
              agentManager.join()
              theAgent.disconnect()
              // no need to send agent any further its got to its target
              // but the node has been reinstated hence need another packet on ring
              toRing.write ( emptyPacket )
            }
            else {
              agentManager.join()
              theAgent.disconnect()
              toRing.write(theAgent)
              println "Node $element: restarting has passed agent on to next node"
            }
          } // end else
        } // end else
      }// end else
    } // end while true
  } // end run
}