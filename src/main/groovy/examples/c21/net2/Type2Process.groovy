package examples.c21.net2
 
import jcsp.lang.*
import groovyJCSP.*
import jcsp.net2.*




class Type2Process extends DynamicMobileProcess implements Serializable {
	
  def toGatherer
  def ChannelInput inChannel
  def int nodeId
  
  def connect (l) {
    inChannel = l[0]
    nodeId = l[1]
    toGatherer = l[2]
  }

  def disconnect () {
    inChannel = null
  }

  void run() {
    def toGathererChannel = NetChannel.any2net(toGatherer)
    while (true) {
      def Type2 d = inChannel.read()
	  //println "T2: $d read data into Type process"
      d.modify(nodeId)
	  //println "T2: $d sending modified data to Gatherer"
      toGathererChannel.write(d)
	  //println "T2: $d have sent modified data to Gatherer"
    }    
  }

}