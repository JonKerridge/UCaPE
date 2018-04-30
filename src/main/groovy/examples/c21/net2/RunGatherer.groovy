package examples.c21.net2

import jcsp.net2.*
import jcsp.net2.tcpip.*
import groovyJCSP.*


def nodeIP = "127.0.0.2"
def nodeAddress = new TCPIPNodeAddress(nodeIP, 3000)
Node.getInstance().init(nodeAddress)
def fromNodesToGatherer = NetChannel.net2one() // cn 50

println "Gatherer Starting"
def processList = new Gatherer ( fromNodes: fromNodesToGatherer )

new PAR ([ processList]).run()
