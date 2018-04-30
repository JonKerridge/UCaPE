package examples.c19.net2.UniversalClient

import jcsp.lang.*
import groovyJCSP.*
import jcsp.util.*


def ipChannel = Channel.one2one(new OverWriteOldestBuffer(5))

new PAR([ new UCInterface(sendNodeIdentity: ipChannel.out()), 
				  new UCCapability(receiveNodeIdentity: ipChannel.in() )]).run()
					
	