package exercises.c3

import jcsp.lang.*
import groovy_jcsp.*

class GSCopy implements CSProcess {
  
  ChannelInput inChannel
  ChannelOutput outChannel0
  ChannelOutput outChannel1
  
  void run () {
     while (true) {
      int i = inChannel.read()
      // output the input value in sequence to each output channel
    }
  }
}
