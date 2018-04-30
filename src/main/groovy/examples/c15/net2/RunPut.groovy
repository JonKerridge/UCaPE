package examples.c15.net2

import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*

def putNodeIP = "127.0.0.1"
def getNodeIP = "127.0.0.2"

def nodeAddr = new TCPIPNodeAddress(putNodeIP,3000)
Node.getInstance().init (nodeAddr)

def getNode = new TCPIPNodeAddress(getNodeIP, 3000)
def comms = NetChannel.one2net(getNode, 50)

def pList = [ new Put ( outChannel: comms ) ]

new PAR ( pList ).run()
