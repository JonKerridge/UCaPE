package examples.c13
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class Read implements CSProcess {
  
  def ChannelOutput r2db
  def ChannelInput db2r
  def int id
  def ChannelOutput toConsole
  
  void run () {
	def timer = new CSTimer()
    toConsole.write ( "Reader $id has started \n")
    for ( i in 0 ..<10 ) {
      def d = new DataObject(pid:id)
      d.location = i
      r2db.write(d)
      d = db2r.read()
      toConsole.write ( "Location " +  d.location + " has value " + d.value + "\n")
      timer.sleep(100)
    }
    toConsole.write ( "Reader $id has finished \n")
  }
}
