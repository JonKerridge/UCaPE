package examples.c24.Distributed.processes

import examples.c24.SingleMachine.methods.defs;

import jcsp.lang.*;
import groovy_jcsp.*;



class parExtractUniqueSequences implements CSProcess{
	def equalMap
	def n
	def startIndex
	def words
    def equalWordMap
	
	void run(){
		defs.extractUniqueSequences ( equalMap, n, 
								      startIndex, words, 
                                      equalWordMap )
	}

}
