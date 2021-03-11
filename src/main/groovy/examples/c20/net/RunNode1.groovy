package examples.c20.net
  
import jcsp.lang.*
import jcsp.net.*
import jcsp.net.tcpip.*
import jcsp.net.cns.*
import groovy_jcsp.*
import jcsp.userIO.*

Node.getInstance().init(new TCPIPNodeFactory())

def int nodeId = 1
def int sentMessages = 150
def int nodes = 4

def fromRingName = "ring1"
def toRingName = "ring2"

println " Node $nodeId: connection from $fromRingName to $toRingName "

def fromRing = CNS.createNet2One(fromRingName)
def toRing = CNS.createAny2Net(toRingName)

def processNode = new AgentElement ( fromRing: fromRing,
                                     toRing: toRing,
                                     element: nodeId,
                                     iterations: sentMessages,
                                     nodes: nodes) 

new PAR ([ processNode]).run()
