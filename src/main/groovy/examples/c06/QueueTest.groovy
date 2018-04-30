package examples.c06

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

import examples.c05.Queue
class QueueTest extends GroovyTestCase {
	
	void testQueue() {
		def QP2Q = Channel.one2one()
		def Q2QC = Channel.one2one()
		def QC2Q = Channel.one2one()
				
		def qProducer = new QProducerForTest ( put: QP2Q.out(), iterations: 50 )
		def queue     = new Queue ( put: QP2Q.in(), get: QC2Q.in(), 
				                    receive: Q2QC.out(), elements: 5)
		def qConsumer = new QConsumerForTest ( get: QC2Q.out(), 
			                                   receive: Q2QC.in() )
		def testList = [ qProducer, queue, qConsumer ]
		new PAR ( testList ).run()
				
		def expected = qProducer.sequence
		def actual = qConsumer.outSequence
		assertTrue(expected == actual)		
	}
}
