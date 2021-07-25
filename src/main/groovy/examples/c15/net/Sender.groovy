package examples.c15.net

import jcsp.lang.*
import groovy_jcsp.*

class Sender implements CSProcess {
  
  ChannelOutput outChannel
  def String id
  
  void run() {
    def timer = new CSTimer()
    while (true) {
      timer.sleep(1000)
      outChannel.write ( id )
    }
  }
}

      
  
