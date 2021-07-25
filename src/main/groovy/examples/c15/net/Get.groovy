package examples.c15.net

import jcsp.lang.*
import groovy_jcsp.*

class Get implements CSProcess {
  
  ChannelInput inChannel
  int id
  
  def void run() {
    def timer = new CSTimer()
    while (true) {
      def v = inChannel.read()
      println "$id .. $v"
      timer.sleep(200 * v)
    }
  }
}

