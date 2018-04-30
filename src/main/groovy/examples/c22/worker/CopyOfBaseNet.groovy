package examples.c22.worker;

import jcsp.lang.*;
import groovyJCSP.*;

import examples.c22.universalClasses.*

class CopyOfBaseNet implements CSProcess {
	
	/*
	 * BaseNet has some slightly different constructor 
	 * properties compared with Base.  It is passed the 
	 * location (net2 address) of the channel end that connects
	 * from the Emitter to the Base as fromEmitterLoc.  This 
	 * channel end is created during the initial phase of RunBase.
	 */
	
	def toEmitter
	def fromEmitterLoc
	def fromEmitter
	def toCollector
	def baseId
	
	void run(){
		// it is required to set up the net2 connections in the final version
		// channelAddress will be net2 location of
		// input channel to Base from Emitter
		toEmitter.write(new InitObject(id: baseId, channelAddress: fromEmitterLoc))
		def startWork = Channel.one2one()
		def workFinished = Channel.one2one()
		def sharedData = []
		def sync = (Signal)fromEmitter.read() //synchronisation signal
		println "Base: $baseId initialised and about to run internal processes"
		def getter = new GetInput ( toEmitter: toEmitter,
									baseId: baseId,
									fromEmitter: fromEmitter,
									toWorker: startWork.out(),
									sharedData: sharedData )
		def worker = new DoWork ( workOn: startWork.in(), 
								  workCompleted: workFinished.out(),
								  workerId: baseId, 
								  sharedData: sharedData )
		def putter = new SendOutput ( workerFinished: workFinished.in(), 
									  toCollector: toCollector,
									  sharedData: sharedData)
		new PAR([getter, worker, putter]).run()
		println "Base: $baseId terminated"
	}

}
