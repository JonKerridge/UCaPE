package examples.c18.net2
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.net2.*
import jcsp.net2.tcpip.*
import groovy_jcsp.*
import jcsp.userIO.*

def rootIP = "127.0.0.1"
def rootAddress = new TCPIPNodeAddress(rootIP, 3000)
Node.getInstance().init(rootAddress)
def fromNodes = NetChannel.net2one()

def String initialValue = Ask.string ( "Initial List Value ? ")
//int nodes = Ask.Int ("Number of nodes (1..8) ? ", 1, 8)
int nodes = 4
def rootNode = new TripRoot ( fromNodes: fromNodes, 
                               nodes: nodes,
                               initialValue: initialValue )

new PAR ( [rootNode] ).run()
