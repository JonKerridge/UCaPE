package examples.c15.net

import jcsp.lang.*
import groovyJCSP.*

class Sender implements CSProcess {
  
  def ChannelOutput outChannel
  def String id
  
  void run() {
    def timer = new CSTimer()
    while (true) {
      timer.sleep(1000)
      outChannel.write ( id )
    }
  }
}

      
  
