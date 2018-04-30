package examples.c20.net
 
import jcsp.lang.*
import groovyJCSP.*

class Receiver implements CSProcess {
	
  def ChannelInput fromElement
  def ChannelOutput outChannel
  def ChannelOutput clear
  def ChannelInput fromConsole
  
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

