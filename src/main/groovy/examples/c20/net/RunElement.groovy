package examples.c20.net
 
import jcsp.lang.*
import jcsp.net.*
import jcsp.net.tcpip.*
import jcsp.net.cns.*
import groovyJCSP.*
import jcsp.userIO.*

Node.getInstance().init(new TCPIPNodeFactory())

def int nodeId = Ask.Int ("Node identification? ", 1, 9)
def boolean last = Ask.Boolean ("Is this the last node? - ( y or n):")
def int sentMessages = Ask.Int("Number of messages to be sent by a Sender? ", 10, 2000)
def int nodes = Ask.Int("Number of nodes? ", 1, 9)

def fromRingName = "ring" + nodeId
def toRingName = (last) ? "ring0" : "ring" + (nodeId+1)

println " Node $nodeId: connection from $fromRingName to $toRingName "

def fromRing = CNS.createNet2One(fromRingName)
def toRing = CNS.createAny2Net(toRingName)
 
def processNode = new AgentElement ( fromRing: fromRing,
                                     toRing: toRing,
                                     element: nodeId,
                                     iterations: sentMessages,
                                     nodes: nodes) 

new PAR ([ processNode]).run()
