package examples.c20.net2
 
import jcsp.lang.*

class Sender implements CSProcess {
	
  ChannelOutput toElement
  int element
  int nodes
  int iterations
  
  def void run() {
    def timer = new CSTimer()
    for ( i in 1 .. iterations ) {
      def dest = (i % (nodes) ) + 2
      if ( dest != element ) {
        def packet = new RingPacket ( source: element, destination: dest , value: (element * 10000) + i , full: true)
        toElement.write(packet)
        timer.sleep(500)
      }
    }
    println "Sender $element has finished"
  }
}

    
