package examples.c15.net

import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import jcsp.userIO.*

Node.getInstance().init(new TCPIPNodeFactory ())

def comms = CNS.createOne2Net ("comms")

def pList = [ new Put ( outChannel: comms ) ]

new PAR ( pList ).run()