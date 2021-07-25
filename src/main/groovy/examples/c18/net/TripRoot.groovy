package examples.c18.net

import jcsp.lang.*
import groovy_jcsp.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*

class TripRoot implements CSProcess{
  
  ChannelInput fromNodes
  def String initialValue
  int nodes
  
  void run() {
    
    def N2A = Channel.one2one()
    def A2N = Channel.one2one()  
    ChannelInput toAgentInEnd = N2A.in()
    ChannelInput fromAgentInEnd = A2N.in()
    ChannelOutput toAgentOutEnd = N2A.out()
    ChannelOutput fromAgentOutEnd = A2N.out()
    
    def tripList = [ fromNodes.getChannelLocation() ]
                     
    for ( i in 0 ..< nodes) {
      def nodeChannelLocation = fromNodes.read()
      tripList << nodeChannelLocation
    }
    def firstNodeLocation = tripList.get(nodes)
    def firstNodeChannel = NetChannelEnd.createOne2Net(firstNodeLocation)
    def theAgent = new TripAgent( tripList: tripList, 
                                   results: [initialValue],
                                   pointer: nodes)
    firstNodeChannel.write(theAgent)
    theAgent = fromNodes.read()
    theAgent.connect ( [fromAgentOutEnd, toAgentInEnd] )
    def agentManager = new ProcessManager (theAgent)
    agentManager.start()
    def returnedResults = fromAgentInEnd.read()
    println "TripRoot: has received $returnedResults "    
    toAgentOutEnd.write (returnedResults)
    agentManager.join()
    theAgent.disconnect()    
  }

}