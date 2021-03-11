package examples.c18.net2
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.net2.*
import jcsp.net2.tcpip.*
import groovy_jcsp.*
import jcsp.userIO.*

def rootIP = "127.0.0.1"
def rootAddress = new TCPIPNodeAddress(rootIP, 3000)
Node.getInstance().init(rootAddress)
def fromRing = NetChannel.net2one()

def int iterations = Ask.Int ("Number of Iterations ? ", 1, 9)
def String initialValue = Ask.string ( "Initial List Value ? ")

def nextNodeIP = "127.0.0.2"
def nextNodeAddress = new TCPIPNodeAddress(nextNodeIP, 3000)
def toRing = NetChannel.one2net(nextNodeAddress, 50)

toRing.write(0)
fromRing.read()

def rootNode = new Root ( inChannel: fromRing, 
                           outChannel: toRing,
                           iterations: iterations,
                           initialValue: initialValue )

new PAR ( [rootNode] ).run()
