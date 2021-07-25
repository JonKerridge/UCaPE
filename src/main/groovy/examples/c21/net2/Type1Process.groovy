package examples.c21.net2

 
import jcsp.lang.*
import groovy_jcsp.*
import jcsp.net2.*



class Type1Process extends DynamicMobileProcess implements Serializable {
	
  def toGatherer
  ChannelInput inChannel
  int nodeId
  
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
      def Type1 d = inChannel.read()
      d.modify(nodeId)
      toGathererChannel.write(d)
    }    
  }

}
