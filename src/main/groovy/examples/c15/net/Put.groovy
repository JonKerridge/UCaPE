package examples.c15.net

import jcsp.lang.*

class Put implements CSProcess {
  
  ChannelOutput outChannel
  
  def void run() {
    int i = 1
    while (true) {
      outChannel.write ( i )
      i = i + 1
    }
  }
}

      
  
