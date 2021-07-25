package examples.c20.net2
 
import jcsp.lang.*
import groovy_jcsp.*





class Receiver implements CSProcess {
	
  ChannelInput fromElement
  ChannelOutput outChannel
  ChannelOutput clear
  ChannelInput fromConsole
  
  def void run() {
    def recAlt = new ALT ([ fromConsole, fromElement])
    def CONSOLE = 0
    def ELEMENT = 1
    while (true) {
      def index = recAlt.priSelect()
      switch (index) {
        case CONSOLE:
          def state = fromConsole.read()
          outChannel.write("\n go to restart")
          clear.write("\n")
          while (state != "go") { 
            state = fromConsole.read()
            outChannel.write("\n go to restart")
            clear.write("\n")
          }
          outChannel.write("\nresuming ...\n")
          break
        case ELEMENT:
          def packet = fromElement.read()
          outChannel.write ("Received: " + packet.toString() + "\n")
          break
      }
    }
  }
}

