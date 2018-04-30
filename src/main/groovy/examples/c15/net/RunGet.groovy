package examples.c15.net

import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import jcsp.userIO.*

def v = Ask.Int ("Get id? ", 1, 9)

Node.getInstance().init(new TCPIPNodeFactory ())

def comms = CNS.createNet2Any ("comms")

def pList = [ new Get ( inChannel: comms , id: v ) ]

new PAR ( pList ).run()