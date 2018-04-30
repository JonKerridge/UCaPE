package examples.c18.net
 
import jcsp.lang.*
import jcsp.net.*
import jcsp.net.tcpip.*
import jcsp.net.cns.*
import groovyJCSP.*
import jcsp.userIO.*

Node.getInstance().init(new TCPIPNodeFactory())

def int iterations = Ask.Int ("Number of Iterations ? ", 1, 9)
def String initialValue = Ask.string ( "Initial List Value ? ")

def fromRingName = "ring0"
def toRingName = "ring1"

def backChannel = NetChannelEnd.createNet2One()
def fromRing = CNS.createNet2One(fromRingName)
def toRing = CNS.createOne2Net(toRingName)

println " ForwardBackRoot: connection from $fromRingName to $toRingName "

def rootNode = new ForwardBackRoot ( inChannel: fromRing, 
                                      outChannel: toRing,
                                      iterations: iterations,
                                      initialValue: initialValue,
                                      backChannel: backChannel)

new PAR ( [rootNode] ).run()