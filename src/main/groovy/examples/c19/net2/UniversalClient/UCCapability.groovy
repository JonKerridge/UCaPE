package examples.c19.net2.UniversalClient

import jcsp.lang.*
import jcsp.net2.*
import jcsp.net2.tcpip.*
import examples.c19.net2.netObjects.*


class UCCapability implements CSProcess {
	
	def ChannelInput receiveNodeIdentity
	def networkBaseIP = "127.0.0."
	
	void run(){
		def clientINodeId = receiveNodeIdentity.read()
		def clientIpAddress = networkBaseIP + clientINodeId
		def nodeAddr = new TCPIPNodeAddress(clientIpAddress,1000)
		Node.getInstance().init(nodeAddr)
		// create channel on which to receive processes from server
		def processReceive = NetChannel.numberedNet2One(2)
		def processReceiveLocation = processReceive.getLocation()
		//create default channel to access server
		def accessAddress = new TCPIPNodeAddress("127.0.0.1",2345)
		def toAccess = NetChannel.any2net(accessAddress, 1)
		def receiveLocation = new ClientLocation (processReceiveLocation: processReceiveLocation)
		toAccess.write(receiveLocation)
		def accessProcess = processReceive.read()
		def pmA = new ProcessManager(accessProcess)
		pmA.start()
		def serviceProcess = processReceive.read()
		def pmS = new ProcessManager(serviceProcess)
		pmS.start()
	}
}
