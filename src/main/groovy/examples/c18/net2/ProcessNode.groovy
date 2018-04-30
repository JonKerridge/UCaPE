package examples.c18.net2

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*


class ProcessNode implements CSProcess{
  
  def ChannelInput inChannel
  def ChannelOutput outChannel
  def int nodeId   

  void run() {
    def N2A = Channel.one2one()
    def A2N = Channel.one2one()  
    def ChannelInput toAgentInEnd = N2A.in()
    def ChannelInput fromAgentInEnd = A2N.in()
    def ChannelOutput toAgentOutEnd = N2A.out()
    def ChannelOutput fromAgentOutEnd = A2N.out()
    def int localValue = nodeId 
	
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
