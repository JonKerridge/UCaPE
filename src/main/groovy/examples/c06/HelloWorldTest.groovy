package examples.c06
  
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

import examples.c02.ProduceHW

class HelloWorldTest extends GroovyTestCase {
		
	void testMessage() {		
		def connect = Channel.one2one()		
		def producer =  new ProduceHW ( outChannel: connect.out() )
		def consumer = new ConsumeHelloForTest ( inChannel: connect.in() )
		
		def processList = [ producer, consumer ]
		new PAR (processList).run() 		
		def expected = "Hello World!!!"			
		def actual = consumer.message		
		assertTrue(expected == actual)
	}
}
