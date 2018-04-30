package examples.c19.net2.accessServer

import jcsp.net2.*;
import jcsp.net2.tcpip.*
import examples.c19.net2.netObjects.*


def serverIP = "127.0.0.1"
	
def accessAddress = new TCPIPNodeAddress(serverIP, 2345)
Node.getInstance().init(accessAddress)
def accessRequestChannel = NetChannel.numberedNet2One(1)
def accessRequestLocation = accessRequestChannel.getLocation()
println "access request location is $accessRequestLocation"
// now create all the request channels to each of the service processes
def groupLocationServerAddress = new TCPIPNodeAddress(serverIP, 3456)
def requestGLservice = NetChannel.one2net(groupLocationServerAddress, 1)
//
def AServerAddress = new TCPIPNodeAddress(serverIP, 4567)
def requestAservice = NetChannel.one2net(AServerAddress, 1)
//
def BServerAddress = new TCPIPNodeAddress(serverIP, 5678)
def requestBservice = NetChannel.one2net(BServerAddress, 1)
//
def CServerAddress = new TCPIPNodeAddress(serverIP, 6789)
def requestCservice = NetChannel.one2net(CServerAddress, 1)

while (true) {
	def clientRequest = accessRequestChannel.read()
	if (clientRequest instanceof ClientLocation) {
		def clientProcessLocation = clientRequest.processReceiveLocation
		def clientProcessChannel = NetChannel.one2net(clientProcessLocation )
		def accessProcess = new  AccessProcess ( accessRequestLocation:accessRequestLocation,
																					   processReceiveLocation: clientProcessLocation)
		clientProcessChannel.write (accessProcess)
	}
	if (clientRequest instanceof ClientRequestData ) {
		def serviceRequired = clientRequest.serviceRequired
		switch (serviceRequired) {
			case "Service - A" :
				requestAservice.write(clientRequest)
				break
			case "Service - B" :
				requestBservice.write(clientRequest)
				break
			case "Service - C" :
				requestCservice.write(clientRequest)
				break
			case "Group Location Service" :
				requestGLservice.write(clientRequest)
				break
		}
	} 
}
