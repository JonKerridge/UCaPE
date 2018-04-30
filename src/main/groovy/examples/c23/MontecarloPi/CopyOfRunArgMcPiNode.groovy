package examples.c23.MontecarloPi

import examples.c23.loaderObjects.*
import jcsp.net2.tcpip.TCPIPNodeAddress
import jcsp.net2.*
import jcsp.net2.mobile.*
import jcsp.lang.*
import groovyJCSP.*

def timer = new CSTimer()
def startTime = timer.read()
def nodeAddr = new TCPIPNodeAddress(1000)
Node.getInstance().init(nodeAddr)
def workerIP = nodeAddr.getIpAddress()
println "Worker is located at $workerIP"

def loadChannel = NetChannel.numberedNet2One(1, new CodeLoadingChannelFilter.FilterRX())
def loadChannelLocation = loadChannel.getLocation()
def hostIP = args[0]
def hostAddr = new TCPIPNodeAddress(hostIP, 1000)
def hostRequest = NetChannel.any2net(hostAddr, 1)
def requestWorker = new RequestWorker (loadLocation: loadChannelLocation, 
									   nodeIP: workerIP)
hostRequest.write(requestWorker)
def requestSentTime = timer.read()
 
def workerObject = (WorkerObject)loadChannel.read()
def workerReadTime = timer.read()
 
def wProcess = (WorkerInterface) workerObject.workerProcess
def inConnections =  workerObject.inConnections
def outConnections =  workerObject.outConnections
 
def inChannels = new ChannelInputList()
inConnections.each{ cn ->
	inChannels.append(NetChannel.numberedNet2One(cn))
}
 
hostRequest.write(new Signal())
loadChannel.read()

def outChannels = new ChannelOutputList()
outConnections.each{ connection ->
	def outNodeAddr = new  TCPIPNodeAddress(connection[0], 1000)
	outChannels.append(NetChannel.any2net(outNodeAddr, connection[1]))
}
wProcess.connect(inChannels, outChannels)
def wPM = new ProcessManager(wProcess)
def workerStartTime = timer.read()
wPM.start()
wPM.join()
def workerEndTime = timer.read()
println "worker has terminated"
hostRequest.write([ startTime, requestSentTime, workerReadTime, 
					workerStartTime, workerEndTime])
