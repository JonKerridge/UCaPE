package examples.c15.net

import jcsp.lang.*

class Put implements CSProcess {
  
  def ChannelOutput outChannel
  
  def void run() {
    def i = 1
    while (true) {
      outChannel.write ( i )
      i = i + 1
    }
  }
}

      
  
