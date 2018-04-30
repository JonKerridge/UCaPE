package examples.c17.test.net

import jcsp.lang.*
import examples.c05.*


class CollectNumbers implements CSProcess {
  
  def ChannelInput inChannel
  def collectedList = []
  def scaledList = [] 
  def iterations = 20

  void run() {
    println "Collector Started"
    for ( i in 1 .. iterations) {
      def result = (ScaledData) inChannel.read()
      collectedList << result.original
      scaledList << result.scaled
    }
    println "Collector Finished"
    //println "Original: ${collectedList}"
    //println "Scaled  : ${scaledList}"
  }

}