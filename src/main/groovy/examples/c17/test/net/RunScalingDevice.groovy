package examples.c17.test.net

import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*

Node.info.setDevice(null)

Node.getInstance().init(new TCPIPNodeFactory ())
  
NetChannelInput ordinaryInput = CNS.createNet2One("ordinaryInput")
NetChannelOutput scaledOutput = CNS.createOne2Net("scaledOutput")

new PAR(new ScalingDevice (inChannel: ordinaryInput, outChannel: scaledOutput) ).run()
 