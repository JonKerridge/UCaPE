package examples.c15.net

import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import jcsp.userIO.*

Node.getInstance().init(new TCPIPNodeFactory ())

def comms = CNS.createNet2One ("comms")

def pList = [ new Receiver ( inChannel: comms ) ]

new PAR ( pList ).run()