package examples.c18.net2
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.net2.*
import jcsp.net2.tcpip.*
import groovyJCSP.*
import jcsp.userIO.*

def int nodeId = Ask.Int ("Node identification (2..9) ? ", 2, 9)
def Boolean last = Ask.Boolean ("Is this the last node? - ( y or n):")
 
def ipBase = "127.0.0."
def nodeIP = ipBase + nodeId
def nodeAddress = new TCPIPNodeAddress(nodeIP, 3000)
Node.getInstance().init(nodeAddress)
def fromRing = NetChannel.net2one()
fromRing.read()

def nextNodeIP = (last) ? "127.0.0.1" : ipBase + (nodeId + 1)

def nextNodeAddress = new TCPIPNodeAddress(nextNodeIP, 3000)
def toRing = NetChannel.one2net(nextNodeAddress, 50)
toRing.write(0)

def processNode = new ProcessNode ( inChannel: fromRing,
                                     outChannel: toRing,
                                     nodeId: nodeId) 

new PAR ([ processNode]).run()



  
