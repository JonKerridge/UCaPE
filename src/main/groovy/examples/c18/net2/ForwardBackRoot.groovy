package examples.c18.net2

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import groovy_jcsp.*
import jcsp.net2.*

class ForwardBackRoot implements CSProcess{
  
  ChannelInput inChannel
  ChannelOutput outChannel
  int iterations
  def String initialValue
  def NetChannelInput backChannel
  
  void run() {
    def N2A = Channel.one2one()
    def A2N = Channel.one2one() 
	 
    ChannelInput toAgentInEnd = N2A.in()
    ChannelInput fromAgentInEnd = A2N.in()
    ChannelOutput toAgentOutEnd = N2A.out()
    ChannelOutput fromAgentOutEnd = A2N.out()

    def backChannelLocation = backChannel.getLocation()
    
    def theAgent = new ForwardBackAgent( results: [initialValue],
                                         backChannel: backChannelLocation)
    
    def rootAlt = new ALT ( [inChannel, backChannel])
	
    outChannel.write(theAgent)
    int i = 1
    def rootValue = -1
    def running = true
	
    while ( running) {
      def index = rootAlt.select()
      switch (index) {
        case 0:		// agent has returned
          theAgent = inChannel.read()
          theAgent.connect ( [fromAgentOutEnd, toAgentInEnd] )
          def agentManager = new ProcessManager (theAgent)
          agentManager.start()
          def forwardLocation = backChannel.read()
          def forwardChannel = NetChannel.one2net(forwardLocation)
          forwardChannel.write (rootValue)
          rootValue = rootValue - 1
          def returnedResults = fromAgentInEnd.read()
          println "Root: Iteration: $i is $returnedResults "    
          returnedResults << "end of " + i
          toAgentOutEnd.write (returnedResults)
          def backValue = backChannel.read()
          agentManager.join()
          theAgent.disconnect()
          i = i + 1
          if (i <= iterations) {
            outChannel.write(theAgent)
          }
          else {
            running = false
          }
          break
		  
        case 1:
          def forwardLocation = backChannel.read()
          def forwardChannel = NetChannel.one2net(forwardLocation)
          forwardChannel.write (rootValue)
          rootValue = rootValue - 1
          def backValue = backChannel.read()
          println "Root: During Iteration $i: received $backValue"
          break
      } // end switch
    } // end while 
  } //  end run
}