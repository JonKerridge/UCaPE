package exercises.c2

import jcsp.lang.*
 
class Multiplier implements CSProcess {
  
  ChannelOutput outChannel
  ChannelInput inChannel
  int factor = 2
  
  void run() {
    int i = inChannel.read()
    while (i > 0) {
      // write i * factor to outChannel
      // read in the next value of i
    }
    outChannel.write(i)
  }
}

    
