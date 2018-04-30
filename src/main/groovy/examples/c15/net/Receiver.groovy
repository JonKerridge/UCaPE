package examples.c15.net

import jcsp.lang.*


class Receiver implements CSProcess {
  
  def ChannelInput inChannel
  
  void run() {
    while (true) {
      def v = inChannel.read()
      println "${v}"
    }
  }
}

