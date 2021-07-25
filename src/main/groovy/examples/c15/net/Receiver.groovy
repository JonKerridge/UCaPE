package examples.c15.net

import jcsp.lang.*


class Receiver implements CSProcess {
  
  ChannelInput inChannel
  
  void run() {
    while (true) {
      def v = inChannel.read()
      println "${v}"
    }
  }
}

