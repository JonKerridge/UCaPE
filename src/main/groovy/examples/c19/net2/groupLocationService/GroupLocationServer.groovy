package examples.c19.net2.groupLocationService

import jcsp.net2.*;
import jcsp.net2.tcpip.*
import groovyJCSP.*
//import examples.19.net2.netObjects


def serverIP = "127.0.0.1"
// each service is located at a different port 	
def groupLocationServerAddress = new TCPIPNodeAddress(serverIP, 3456)
Node.getInstance().init(groupLocationServerAddress)
def initialChannel = NetChannel.numberedNet2One(1)
def requestChannel = NetChannel.numberedNet2One(2)
def requestLocation = requestChannel.getLocation()
def glAlt = new ALT([initialChannel, requestChannel])
def locationMap = [:]
while (true) {
	switch (glAlt.select()){
		case 0: // initial request
			def request = initialChannel.read()
			def processSendChannel =NetChannel.one2net(request.processReceiveLocation)
			def glProcess = new GLprocess(requestLocation: requestLocation)
			processSendChannel.write(glProcess)	
			break
		case 1: // request from user
			def requestData = requestChannel.read()
			def responseChannel = NetChannel.one2net(requestData.responseLocation)
			def groupName = requestData.groupName
			if ( locationMap.containsKey(groupName)){
				requestData.location = locationMap[groupName]
				responseChannel.write(requestData)
			}
			else {
				requestData.location == null
				responseChannel.write(requestData)
				requestData = requestChannel.read()
				groupName = requestData.groupName
				location = requestData.location
				locationMap.put(groupName, location)
				println "location map = $locationMap"
			}
			break
	}
}
