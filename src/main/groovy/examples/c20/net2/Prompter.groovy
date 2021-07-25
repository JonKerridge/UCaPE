package examples.c20.net2
 
import jcsp.lang.*

class Prompter implements CSProcess{
  
  ChannelOutput toQueue
  ChannelInput fromQueue
  ChannelOutput toReceiver

  void run() {
    while (true) {
      toQueue.write(1)
      toReceiver.write ( fromQueue.read() )
    }    
  }
}
