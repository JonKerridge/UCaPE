package examples.c15.net

import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import examples.c12.canteen.*


Node.getInstance().init(new TCPIPNodeFactory ())

def cooked = CNS.createNet2One ("COOKED")
def getOne = CNS.createNet2One ("GETONE")
def gotOne = CNS.createOne2Net ("GOTONE")

def processList = [
  new ClockedQueuingServery(service:getOne, deliver:gotOne, supply:cooked)
  ]

new PAR ( processList ).run()     
