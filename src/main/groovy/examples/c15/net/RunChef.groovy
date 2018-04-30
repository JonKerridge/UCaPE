package examples.c15.net

import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import jcsp.userIO.*
import examples.c12.canteen.*

Node.getInstance().init(new TCPIPNodeFactory ())

def cooked = CNS.createOne2Net ("COOKED")
    
def processList = [ new Kitchen ( supply: cooked) ]

new PAR ( processList ).run()     