package exercises.c3

import jcsp.lang.*
import groovyJCSP.*

class GSCopy implements CSProcess {
  
  def ChannelInput inChannel
  def ChannelOutput outChannel0
  def ChannelOutput outChannel1
  
  void run () {
     while (true) {
      def i = inChannel.read()
      // output the input value in sequence to each output channel
    }
  }
}
