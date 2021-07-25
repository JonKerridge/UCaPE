package examples.c20.net
 
import jcsp.lang.*
import jcsp.net.*
import jcsp.net.tcpip.*
import jcsp.net.cns.*
import groovy_jcsp.*
import jcsp.userIO.*

Node.getInstance().init(new TCPIPNodeFactory())

int nodeId = 2
int sentMessages = 150
int nodes = 4

def fromRingName = "ring2" 
def toRingName = "ring3"

println " Node $nodeId: connection from $fromRingName to $toRingName "

def fromRing = CNS.createNet2One(fromRingName)
def toRing = CNS.createAny2Net(toRingName)

def processNode = new AgentElement ( fromRing: fromRing,
                                     toRing: toRing,
                                     element: nodeId,
                                     iterations: sentMessages,
                                     nodes: nodes) 

new PAR ([ processNode]).run()
