package examples.c15.net2
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*

def getNodeIP = "127.0.0.2"

def getNodeAddr = new TCPIPNodeAddress(getNodeIP, 3000)
Node.getInstance().init (getNodeAddr)

def comms = NetChannel.net2one()
def pList = [ new Get ( inChannel: comms , id: 0 ) ]

new PAR ( pList ).run()
