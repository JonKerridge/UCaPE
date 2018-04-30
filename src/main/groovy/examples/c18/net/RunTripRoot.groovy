package examples.c18.net

import jcsp.lang.*
import jcsp.net.*
import jcsp.net.tcpip.*
import jcsp.net.cns.*
import groovyJCSP.*
import jcsp.userIO.*

Node.getInstance().init(new TCPIPNodeFactory())

def String initialValue = Ask.string ( "Initial List Value ? ")
def int nodes = Ask.Int ("Number of nodes? ", 1, 9)

def fromNodes = CNS.createNet2One("toRoot")

println " Root: input channel created "

def rootNode = new TripRoot ( fromNodes: fromNodes, 
                               nodes: nodes,
                               initialValue: initialValue )

new PAR ( [rootNode] ).run()
