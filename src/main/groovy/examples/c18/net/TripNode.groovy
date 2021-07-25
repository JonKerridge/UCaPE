package examples.c18.net

import jcsp.lang.*
import jcsp.net.*

class TripNode implements CSProcess{
  
  ChannelOutput toRoot
  int nodeId
  
  def N2A = Channel.one2one()
  def A2N = Channel.one2one()  

  void run() {
    ChannelInput toAgentInEnd = N2A.in()
    ChannelInput fromAgentInEnd = A2N.in()
    ChannelOutput toAgentOutEnd = N2A.out()
    ChannelOutput fromAgentOutEnd = A2N.out()
    
    def agentInputChannel = NetChannelEnd.createNet2One()
    toRoot.write ( agentInputChannel.getChannelLocation())
    def theAgent = agentInputChannel.read()
    theAgent.connect ( [fromAgentOutEnd, toAgentInEnd] )
    def agentManager = new ProcessManager (theAgent)
    agentManager.start()
    def currentList = fromAgentInEnd.read()
    currentList << nodeId
    toAgentOutEnd.write (currentList)
    agentManager.join()
  }

}