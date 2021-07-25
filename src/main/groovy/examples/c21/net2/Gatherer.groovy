package examples.c21.net2
 
import jcsp.lang.*






class Gatherer implements CSProcess{
  ChannelInput fromNodes

  void run() {
    while (true) {
      def d = fromNodes.read()
      println "Gathered from ${d.toString()}"      
    }    
  }
}
