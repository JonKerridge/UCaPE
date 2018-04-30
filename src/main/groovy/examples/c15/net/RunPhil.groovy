package examples.c15.net

import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import jcsp.userIO.*
import examples.c12.canteen.*

Node.getInstance().init(new TCPIPNodeFactory ())

def gotOne = CNS.createNet2Any ("GOTONE")
def getOne = CNS.createAny2Net ("GETONE")

    def philList = ( 0 .. 4 ).collect{
      i -> return new Philosopher(philosopherId:i, service:getOne, deliver:gotOne)}

new PAR ( philList ).run()     