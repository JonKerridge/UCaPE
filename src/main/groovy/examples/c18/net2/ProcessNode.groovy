package examples.c18.net2

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*


class ProcessNode implements CSProcess{
  
  ChannelInput inChannel
  ChannelOutput outChannel
  int nodeId

  void run() {
    def N2A = Channel.one2one()
    def A2N = Channel.one2one()  
    ChannelInput toAgentInEnd = N2A.in()
    ChannelInput fromAgentInEnd = A2N.in()
    ChannelOutput toAgentOutEnd = N2A.out()
    ChannelOutput fromAgentOutEnd = A2N.out()
    int localValue = nodeId
	
    while (true) {
      def theAgent = inChannel.read()
      theAgent.connect ( [fromAgentOutEnd, toAgentInEnd] )
      def agentManager = new ProcessManager (theAgent)
      agentManager.start()
      def currentList = fromAgentInEnd.read()
      currentList << localValue
	  println "Node $nodeId: list = $currentList"
      toAgentOutEnd.write (currentList)
      agentManager.join()
      theAgent.disconnect()
      outChannel.write(theAgent)
      localValue = localValue + 10      
    }
  }
}
