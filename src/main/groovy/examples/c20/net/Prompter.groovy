package examples.c20.net
 
import jcsp.lang.*
import groovy_jcsp.*

class Prompter implements CSProcess{
  
  def ChannelOutput toQueue
  def ChannelInput fromQueue
  def ChannelOutput toReceiver

  void run() {
    while (true) {
      toQueue.write(1)
      toReceiver.write ( fromQueue.read() )
    }    
  }
}