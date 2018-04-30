package examples.c18.net
 
import jcsp.lang.*
import jcsp.net.*
import jcsp.net.tcpip.*
import jcsp.net.cns.*
import groovyJCSP.*
import jcsp.userIO.*

Node.getInstance().init(new TCPIPNodeFactory())

def int nodeId = Ask.Int ("Node identification? ", 1, 9)

def toRoot = CNS.createAny2Net("toRoot")

def processNode = new TripNode ( toRoot: toRoot,
                                  nodeId: nodeId) 


new PAR ([processNode]).run()



  
