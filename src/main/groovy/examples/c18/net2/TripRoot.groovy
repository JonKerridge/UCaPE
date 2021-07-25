package examples.c18.net2

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import jcsp.net2.*

class TripRoot implements CSProcess{
  
  ChannelInput fromNodes
  def String initialValue
  int nodes
  
  void run() {    
    def  N2A = Channel.one2one()
    def  A2N = Channel.one2one()  
    ChannelInput toAgentInEnd = N2A.in()
    ChannelInput fromAgentInEnd = A2N.in()
    ChannelOutput toAgentOutEnd = N2A.out()
    ChannelOutput fromAgentOutEnd = A2N.out()
    
    def tripList = [ fromNodes.getLocation() ]
                     
    for ( i in 0 ..< nodes) {
      def nodeChannelLocation = fromNodes.read()
      tripList << nodeChannelLocation
    }
    def firstNodeLocation = tripList.get(nodes)
    def firstNodeChannel = NetChannel.one2net(firstNodeLocation)
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
