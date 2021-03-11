package examples.c24.SingleMachine.supportProcesses

import examples.c24.SingleMachine.methods.defs;

import jcsp.lang.*;
import groovy_jcsp.*;



class parSequencer implements CSProcess {
	def n           // the value of n for this sequence
	def inList
	def outList
	
	void run(){	
		defs.sequencer(n, inList, outList)
	}
}
