package examples.c10

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class ExtraElementv2 implements CSProcess { 
	
  ChannelInput fromRing
  ChannelOutput toRing
  
  void run () {
    println "Extra Element v2 starting ..."
    while (true) {
      toRing.write( fromRing.read() )   
    }
  }
}
 
      
