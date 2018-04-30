package examples.c21.net2
 
import jcsp.lang.*
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*



class NodeProcess implements CSProcess {
  def int nodeId		
  def int nodeIPFinalPart  // forms last part of IP address
  def String toGathererIP
  def String toDataGenIP
  def processList = null
  def vanillaList = null // these must be identical initially

  void run() {
    def nodeIP = "127.0.0." + nodeIPFinalPart
    def nodeAddress = new TCPIPNodeAddress(nodeIP, 3000)
    Node.getInstance().init(nodeAddress)
    
    def dataGenAddress =  new TCPIPNodeAddress(toDataGenIP, 3000)
    def toDataGen = NetChannel.any2net(dataGenAddress, 50)	   //50
    def gathererAddress = new TCPIPNodeAddress(toGathererIP, 3000)
    def toGatherer = NetChannel.any2net(gathererAddress, 50)  //51
    def fromDataGen = NetChannel.net2one()		                //52
    def agentVisitChannel= NetChannel.net2one()		            //53
    def agentVisitChannelLocation = agentVisitChannel.getLocation()
    def agentReturnChannel= NetChannel.net2one()	            //54
    def agentReturnChannelLocation = agentReturnChannel.getLocation()
    println "NP: $nodeId, Visit Channel = $agentVisitChannelLocation"
    println "NP: $nodeId, Return Channel = $agentReturnChannelLocation"
	
    toDataGen.write( new DataGenList ( dgl: [ fromDataGen.getLocation(), 
                                               agentVisitChannelLocation,
                                               nodeId] ) )
                                               
    def connectChannels = [ ]
    def typeOrder = [ ]
    def vanillaOrder = [ ]
    def currentSearches = [ ]
    def cp = 0
    
    if (processList != null) {                 
      for ( i in 0 ..< processList.size) {
        def processType = processList[cp].getClass().getName()
        def typeName = processType.substring(0, processType.indexOf("Process"))
        typeOrder << typeName 
        vanillaOrder << typeName 
        connectChannels[cp] = Channel.one2one()
        def pList = [connectChannels[cp].in(), nodeId, toGatherer.getLocation()]
        processList[cp].connect(pList)
        def pm = new ProcessManager(processList[cp])
        pm.start()
        cp = cp + 1
      }
    }
    
    def NodeToInitialAgent = Channel.one2one()
    def NodeToVisitingAgent = Channel.one2one()
    def NodeFromVisitingAgent = Channel.one2one()
    def NodeFromReturningAgent = Channel.one2one()
    
    def NodeToInitialAgentInEnd = NodeToInitialAgent.in()
    def NodeToVisitingAgentInEnd = NodeToVisitingAgent.in()
    def NodeFromVisitingAgentOutEnd = NodeFromVisitingAgent.out()
    def NodeFromReturningAgentOutEnd = NodeFromReturningAgent.out()
    
    def myAgent = new AdaptiveAgent()
    myAgent.connect([NodeToInitialAgentInEnd, agentReturnChannelLocation, nodeId])
    def initialPM = new ProcessManager(myAgent)
    initialPM.start()
    
    def nodeAlt = new ALT([fromDataGen, agentVisitChannel, agentReturnChannel])
    def currentVisitList = [ ] 
                             
    while (true) {
      switch ( nodeAlt.select() ) {
        case 0: // data or update to available nodes
          def d = fromDataGen.read()
          if ( d instanceof AvailableNodeList ) {
            currentVisitList = [ ]
            for ( i in 0 ..< d.anl.size) { 
              if (d.anl[i].toString() != agentVisitChannelLocation.toString())
                currentVisitList << d.anl[i] 
            }
            NodeToInitialAgent.out().write(currentVisitList)
          } 
          else {  // must be a data type name 
            def dType = d.getClass().getName()
            if ( typeOrder.contains(dType) ) {
              def i = 0
              def notFound = true
              while (notFound) {
                if (typeOrder[i] == dType) {
                  notFound = false
                }
                else {
                  i = i + 1
                }
              }
              connectChannels[i].out().write(d)
             }
            else {  // do not have process for this data type
              if ( ! currentSearches.contains(dType)) {
                currentSearches << dType
                NodeToInitialAgent.out().write(dType)
                initialPM.join()
                myAgent = new AdaptiveAgent()
                myAgent.connect([NodeToInitialAgentInEnd, agentReturnChannelLocation, 
                                 nodeId])
                initialPM = new ProcessManager(myAgent)
                initialPM.start()
                NodeToInitialAgent.out().write(currentVisitList)
              }
            }            
          }
          break
        case 1: // visiting agent has arrived
          def visitingAgent = agentVisitChannel.read()
          visitingAgent.connect([NodeToVisitingAgentInEnd, 
                                 NodeFromVisitingAgentOutEnd ])
          def visitPM = new ProcessManager(visitingAgent)
          visitPM.start()
          def typeRequired = NodeFromVisitingAgent.in().read()
          if ( vanillaOrder.contains(typeRequired) ) {
            def i = 0
            def notFound = true
            while (notFound) {
              if (vanillaOrder[i] == typeRequired) {
                notFound = false
              }
              else {
                i = i + 1
              }            
            }
            NodeToVisitingAgent.out().write(vanillaList[i])
            def agentHome = NodeFromVisitingAgent.in().read()
          }
          else {  // do not have process for this data type
            NodeToVisitingAgent.out().write(null)              
          }
          visitPM.join()
          break
        case 2: // agent has returned to originating node
          def returnAgent = agentReturnChannel.read()
          returnAgent.connect([NodeFromReturningAgentOutEnd])
          def returnPM = new ProcessManager (returnAgent)
          returnPM.start()
          def returnList = NodeFromReturningAgent.in().read()
          returnPM.join()
          def returnedType = returnList[1]
          currentSearches.remove([returnedType])
          typeOrder << returnList[1] 
          connectChannels[cp] = Channel.one2one()
          processList << returnList[0]               
          def pList = [connectChannels[cp].in(), nodeId, toGatherer.getLocation()]
          processList[cp].connect(pList)
          def pm = new ProcessManager(processList[cp])
          cp = cp + 1
          pm.start()          
          break
      } // end switch    
    } // end while true    
  } // end run

}
