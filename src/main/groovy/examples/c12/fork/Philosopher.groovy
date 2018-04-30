package examples.c12.fork

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class Philosopher implements CSProcess {
  
  def ChannelOutput leftFork
  def ChannelOutput rightFork
  def ChannelOutput enter
  def ChannelOutput exit
  def int id
  
  def timer = new CSTimer()
  
  def void action ( id, type, delay ) {
    println "${type} : ${id} "
    timer.sleep(delay)
  }
  
  void run() {
     while (true) {
      action (id, "            thinking", 1000 )
      enter.write(1)
      println "$id: entered"
      leftFork.write(1)
      println "$id: got left fork"
      rightFork.write(1)
      println "$id: got right fork"
      action (id, "            eating", 2000 )
      leftFork.write(1)
      println "$id: put down left"
      rightFork.write(1)
      println "$id: put down right"
      exit.write(1)
      println "$id: exited"
    }
  }
}

      
