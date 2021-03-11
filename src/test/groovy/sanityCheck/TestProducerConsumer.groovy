package sanityCheck

import groovy_jcsp.PAR

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.Channel

class TestProducerConsumer extends GroovyTestCase {
		
	void testMessage() {		
		def connect = Channel.one2one()		
		def producer =  new Producer ( outChannel: connect.out() )
		def consumer = new Consumer ( inChannel: connect.in() )
		
		def processList = [ producer, consumer ]
		new PAR (processList).run() 		
		assertTrue(producer.produceList == consumer.consumeList)
	}
}
