package examples.c10

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class ExtraElementv2 implements CSProcess { 
	
  def ChannelInput fromRing
  def ChannelOutput toRing
  
  void run () {
    println "Extra Element v2 starting ..."
    while (true) {
      toRing.write( fromRing.read() )   
    }
  }
}
 
      
