package examples.c24.SingleMachine.supportProcesses

import examples.c24.SingleMachine.methods.defs;

import jcsp.lang.*;
import groovyJCSP.*;



class parFindEqualKeys implements CSProcess {
	def words
	def startIndex
	def inList
	def outMap
	
	void run(){	
		defs.extractEqualValues(words, startIndex, inList, outMap)
	}

}
