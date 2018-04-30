package examples.c12.canteen

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class Clock implements CSProcess {
  
  def ChannelOutput toConsole
  
  void run() {

    def tim = new CSTimer()
    def tick = 0

    while (true) {
      toConsole.write ("Time: $tick \n")
      tim.sleep (1000)
      tick = tick + 1
    }

  }

}
