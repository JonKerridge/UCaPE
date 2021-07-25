package exercises.c9

import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class Blender implements CSProcess {

	ChannelInput fromConsole
	ChannelOutput toConsole
	ChannelOutput clearConsole
	ChannelOutput toManager
	ChannelInput fromManager
	
	void run(){
		while (true) {
			toConsole.write("Input an r when Blender ready\n")
			fromConsole.read()  //this reads the enabling r from the console
			toManager.write(1)  // informs the manager the blender is ready
			fromManager.read()  // manager indicates that blending can start
			clearConsole.write("\n")  // clrea the console input area
			toConsole.write("Blending\n") // and write an appropriate message
			toConsole.write("Input an f when Blender finished\n")
			fromConsole.read() // input the f indicating that blending has finished
			toManager.write(2) // and tell the manager that blending has finished
			clearConsole.write("\n")
			fromManager.read() // manager confirms to the blender finish and all hoppers have stopped emptying
			toConsole.write("Cycle complete\n")
		}
		
	}	
}
