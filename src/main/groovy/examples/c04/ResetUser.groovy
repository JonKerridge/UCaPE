package examples.c04
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

class ResetUser implements CSProcess {
	
  def ChannelOutput resetValue
  def ChannelOutput toConsole
  def ChannelInput fromConverter
  def ChannelOutput toClearOutput
	
  void run() {
	toConsole.write( "Please input reset values\n" )
	while (true) {
	  def v = fromConverter.read()
	  toClearOutput.write("\n")
	  resetValue.write(v)
	}
  }
}
