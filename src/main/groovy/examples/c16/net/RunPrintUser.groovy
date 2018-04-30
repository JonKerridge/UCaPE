package examples.c16.net
 
import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import jcsp.userIO.*

def user = Ask.Int ("User Number ? ", 1, 999)

Node.info.setDevice(null)

Node.getInstance().init(new TCPIPNodeFactory ())

def pRequest = CNS.createAny2Net ("REQUEST")
def pRelease = CNS.createAny2Net ("RELEASE")

new PAR ( [ new PrintUser ( printerRequest: pRequest, 
                            printerRelease: pRelease, 
                            userId : user
                          )
          ] ).run()

