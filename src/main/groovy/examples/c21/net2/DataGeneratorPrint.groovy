package examples.c21.net2
 
import jcsp.lang.*
import groovyJCSP.*
import jcsp.net2.*
//import jcsp.net2.cns.*
//import jcsp.net2.tcpip.*

class DataGeneratorPrint implements CSProcess {
  def ChannelInput fromNodes
  def interval = 1000

  void run() {
    def ChannelOutputList toNodes = new ChannelOutputList()
    def agentVisitChannelList = [ ]
    def allocationList = [ ]
    def rng = new Random()
    def timer = new CSTimer()
    def dgAlt = new ALT ([fromNodes, timer])
    def type1Instance = 1000
    def type2Instance = 2000
    def type3Instance = 3000
    def instanceValue = 0
    def nodesRegistered = 0
    while (true) {
      def checkingForNewNodes = true
      def nodeAppended = false
      timer.setAlarm (timer.read() + interval)
      while (checkingForNewNodes || (nodesRegistered < 3)){
        switch ( dgAlt.select()) {
          case 0:
            def nodeData = fromNodes.read()
            toNodes.append ( NetChannel.one2net ( nodeData.dgl[0] ) )
            agentVisitChannelList << nodeData.dgl[1]
            allocationList << nodeData.dgl[2]                                      
            nodesRegistered = nodesRegistered + 1
            nodeAppended = true
			println "DG: node appended ${nodeData.dgl[2]}"
            break
          case 1:
            checkingForNewNodes = false
            break
        }        
      }
      // needs to be a serializable object
      if (nodeAppended) { 
        toNodes.write(new AvailableNodeList ( anl: agentVisitChannelList)) 
        println "DG: written AVCL of size ${agentVisitChannelList.size}"
      }
      def nNodes = toNodes.size()
      def nodeId = rng.nextInt(nNodes)
      switch ( rng.nextInt(3) + 1) {
        case 1:
          toNodes[nodeId].write ( new Type1 ( typeInstance: type1Instance, 
                                               instanceValue: instanceValue ))
          println "DG: written Type1: $type1Instance, seq: $instanceValue to node: ${allocationList[nodeId]}"
          type1Instance = type1Instance + 1
          break
        case 2:
          toNodes[nodeId].write ( new Type2 ( typeInstance: type2Instance, 
                                               instanceValue: instanceValue ))
          println "DG: written Type2: $type2Instance, seq: $instanceValue to node: ${allocationList[nodeId]}"
          type2Instance = type2Instance + 1
          break
        case 3:
          toNodes[nodeId].write ( new Type3 ( typeInstance: type3Instance, 
                                               instanceValue: instanceValue ))
          println "DG: written Type3: $type3Instance, seq: $instanceValue to node: ${allocationList[nodeId]}"
          type3Instance = type3Instance + 1
          break
      }
      instanceValue = instanceValue + 1
    }
    
  }

}