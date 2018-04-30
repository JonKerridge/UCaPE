package examples.c20.net
   
import jcsp.lang.*
import jcsp.net.*
import jcsp.net.tcpip.*
import jcsp.net.cns.*
import groovyJCSP.*
import jcsp.userIO.*

Node.getInstance().init(new TCPIPNodeFactory())

def fromRingName = "ring0"
def toRingName = "ring1"

println " Node 0: connection from $fromRingName to $toRingName "

def fromRing = CNS.createNet2One(fromRingName)
def toRing = CNS.createAny2Net(toRingName)

def processNode = new AgentExtraElement ( fromRing: fromRing,
                                           toRing: toRing )

new PAR ([ processNode]).run()
