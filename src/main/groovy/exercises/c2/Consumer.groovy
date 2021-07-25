package exercises.c2
 
import jcsp.userIO.*
import jcsp.lang.*


class Consumer implements CSProcess {
  
  ChannelInput inChannel
  
  void run() {
    int i = inChannel.read()
    while ( i > 0 ) {
      //insert a modified println statement
      i = inChannel.read()
    }
    println "Finished"
  }
}

