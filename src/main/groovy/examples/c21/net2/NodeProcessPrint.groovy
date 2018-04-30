package examples.c21.net2
 
import jcsp.lang.*
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*



class NodeProcessPrint implements CSProcess {
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
        //println "NP-$nodeId: constructed with a process for $typeName as $cp"
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
    //println "NP-$nodeId: initial typeOrder is $typeOrder"
    //println "NP-$nodeId: vanilla Order is $vanillaOrder"
    
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
        case 0:
          def d = fromDataGen.read()
          if ( d instanceof AvailableNodeList ) {
			//println "NP-$nodeId: AvailableNodeList:"
			//d.anl.each{println "$it"}
            //d.anl.remove(agentVisitChannelLocation)  // list will contain this node's visit location
            currentVisitList = [ ]
            for ( i in 0 ..< d.anl.size) { 
				if (d.anl[i].toString() != agentVisitChannelLocation.toString())
				  currentVisitList << d.anl[i] 
			}
            NodeToInitialAgent.out().write(currentVisitList)
            println "NP-$nodeId: received visit list from DataGen with ${currentVisitList.size} elements"
			currentVisitList.each {println "$it"}
          }
//        must be a data type name 
          else { 
            def dType = d.getClass().getName()
            //println "NP-$nodeId: data of type $dType being processed"
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
              //println "NP-$nodeId: [${d.toString()}] sent to process $i in order"
              connectChannels[i].out().write(d)
              //println "NP-$nodeId: data of type $dType sent to Gatherer from $i'th process"
             }
            else {  
              // do not have process for this data type
              if ( ! currentSearches.contains(dType)) {
                currentSearches << dType
                //println "NP-$nodeId: data of type $dType search initiated"
                NodeToInitialAgent.out().write(dType)
                initialPM.join()
                // create a new initial agent
                myAgent = new AdaptiveAgent()
                myAgent.connect([NodeToInitialAgentInEnd, agentReturnChannelLocation, nodeId])
                initialPM = new ProcessManager(myAgent)
                initialPM.start()
                NodeToInitialAgent.out().write(currentVisitList)
                //println "NP-$nodeId: myAgent re created"
              }
              else {
                //println "NP-$nodeId: data of type $dType already being searched for"                
              }
            }            
          }
          break
        case 1:
          //println "NP-$nodeId: visiting agent has arrived"
          def visitingAgent = agentVisitChannel.read()
          visitingAgent.connect([NodeToVisitingAgentInEnd, 
                                 NodeFromVisitingAgentOutEnd ])
          def visitPM = new ProcessManager(visitingAgent)
          visitPM.start()
          def typeRequired = NodeFromVisitingAgent.in().read()
          //println "NP-$nodeId: visiting agent wants $typeRequired"
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
            // the agent will have disconnected the internal connection
            //hence we must remake the connection net2 time we enter the loop
            //println "NP-$nodeId: process for $typeRequired written to agent from $agentHome"
          }
          else {  // do not have process for this data type
            //println "NP-$nodeId: process for $typeRequired not available here"
            NodeToVisitingAgent.out().write(null)              
          }
          visitPM.join()
          break
        case 2:
          //println "NP-$nodeId: returned agent has arrived"
          def returnAgent = agentReturnChannel.read()
          returnAgent.connect([NodeFromReturningAgentOutEnd])
          def returnPM = new ProcessManager (returnAgent)
          returnPM.start()
          def returnList = NodeFromReturningAgent.in().read()
          returnPM.join()
          def returnedType = returnList[1]
          //println "NP-$nodeId: returned agent has brought process for $returnedType"
          currentSearches.remove([returnedType])
          typeOrder << returnList[1] 
          connectChannels[cp] = Channel.one2one()
          processList << returnList[0]               
          def pList = [connectChannels[cp].in(), nodeId, toGatherer.getLocation()]
          processList[cp].connect(pList)
          def pm = new ProcessManager(processList[cp])
          //println "NP-$nodeId: revised typeOrder is $typeOrder"
          //println "NP-$nodeId: has started $returnedType Process"
          cp = cp + 1
          pm.start()          
          break
      }    
    }    
  }

}