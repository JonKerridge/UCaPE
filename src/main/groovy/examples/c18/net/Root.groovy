package examples.c18.net

import jcsp.lang.*
import groovy_jcsp.*

class Root implements CSProcess{
  
  ChannelInput inChannel
  ChannelOutput outChannel
  int iterations
  def String initialValue  

  void run() {
    def N2A = Channel.one2one()
    def A2N = Channel.one2one()  
    ChannelInput toAgentInEnd = N2A.in()
    ChannelInput fromAgentInEnd = A2N.in()
    ChannelOutput toAgentOutEnd = N2A.out()
    ChannelOutput fromAgentOutEnd = A2N.out()

    def theAgent = new Agent( results: [initialValue])
    
    for ( i in 1 .. iterations) {
      outChannel.write(theAgent)
      theAgent = inChannel.read()
      theAgent.connect ( [fromAgentOutEnd, toAgentInEnd ] )
      def agentManager = new ProcessManager (theAgent)
      agentManager.start()
      def returnedResults = fromAgentInEnd.read()
      println "Root: Iteration: $i is $returnedResults "    
      returnedResults << "end of " + i
      toAgentOutEnd.write (returnedResults)
      agentManager.join()
      theAgent.disconnect()
    }
  }
}