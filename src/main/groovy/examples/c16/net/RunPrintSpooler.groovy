package examples.c16.net
  
import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import jcsp.userIO.*

def spoolers = Ask.Int ("Number of spoolers ? ", 1, 9)

Node.info.setDevice(null)

Node.getInstance().init(new TCPIPNodeFactory ())

def pRequest = CNS.createNet2One ("REQUEST")
def pRelease = CNS.createNet2One ("RELEASE")

new PAR ( [ new PrintSpooler ( printerRequest: pRequest, 
                               printerRelease: pRelease, 
                               spoolers : spoolers  
                             )
          ] ).run()

