package examples.c04
 
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*

class ResetUser implements CSProcess {
	
  ChannelOutput resetValue
  ChannelOutput toConsole
  ChannelInput fromConverter
  ChannelOutput toClearOutput
	
  void run() {
	toConsole.write( "Please input reset values\n" )
	while (true) {
	  def v = fromConverter.read()
	  toClearOutput.write("\n")
	  resetValue.write(v)
	}
  }
}
