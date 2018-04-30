package examples.c15.net

import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import jcsp.userIO.*

Node.getInstance().init(new TCPIPNodeFactory ())

def comms = CNS.createNet2Any ("comms")

def pList = (0 .. 5).collect{i -> new Get ( inChannel: comms, id: i ) }

new PAR ( pList ).run()