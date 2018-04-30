package examples.c15.net

import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import jcsp.userIO.*

def v= Ask.string ("Sender identity string ? ")

Node.getInstance().init(new TCPIPNodeFactory ())

def comms = CNS.createAny2Net ("comms")

def pList = [ new Sender ( outChannel: comms, id: v ) ]

new PAR ( pList ).run()
