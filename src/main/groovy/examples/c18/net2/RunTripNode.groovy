package examples.c18.net2
 // copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.net2.*
import jcsp.net2.tcpip.*
import groovyJCSP.*
import jcsp.userIO.*

def int nodeId = Ask.Int ("Node identification (2..9)? ", 2, 9)

def ipBase = "127.0.0."
def nodeIP = ipBase + nodeId
def nodeAddress = new TCPIPNodeAddress(nodeIP, 3000)
Node.getInstance().init(nodeAddress)

def rootNodeIP = "127.0.0.1"
def rootNodeAddress = new TCPIPNodeAddress(rootNodeIP, 3000)

def toRoot = NetChannel.any2net(rootNodeAddress, 50)

def processNode = new TripNode ( toRoot: toRoot,
                                  nodeId: nodeId) 

new PAR ([processNode]).run()



  
