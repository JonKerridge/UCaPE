package examples.c23
import examples.c23.loaderObjects.RequestWorker;
import examples.c23.loaderObjects.Signal;
import examples.c23.loaderObjects.WorkerInterface;
import examples.c23.loaderObjects.WorkerObject;

import jcsp.net2.tcpip.TCPIPNodeAddress
import jcsp.net2.*
import jcsp.net2.mobile.*
//import netObjects.*
import jcsp.lang.*
import groovyJCSP.*
import jcsp.userIO.*

//def nodeAddr4 = Ask.Int( "what is the fourth part of the node's IP-address?  ", 2, 254)
println "running $args"
def nodeAddr4 = args[0]
def timer = new CSTimer()
def startTime = timer.read()
def nodeIP = "127.0.0." + nodeAddr4
def nodeAddr = new TCPIPNodeAddress(nodeIP, 1000)
//def nodeAddr = new TCPIPNodeAddress(1000)  // create nodeAddr for most global IP address
// create node instance 
Node.getInstance().init(nodeAddr)
def workerIP = nodeAddr.getIpAddress()
println "Worker is located at $workerIP"
// create load channel
def loadChannel = NetChannel.numberedNet2One(1, new CodeLoadingChannelFilter.FilterRX())
def loadChannelLocation = loadChannel.getLocation()
// make connection to host
// could be passed in as an argument
def hostIP = "127.0.0.1"
def hostAddr = new TCPIPNodeAddress(hostIP, 1000)
// create host request channel
def hostRequest = NetChannel.any2net(hostAddr, 1)
// send request for worker to host
println "Sending request to host"
def requestWorker = new RequestWorker (loadLocation: loadChannelLocation, 
									   nodeIP: workerIP)
hostRequest.write(requestWorker)
def requestSentTime = timer.read()
// read worker from host using load channel
def workerObject = (WorkerObject)loadChannel.read()
def workerReadTime = timer.read()
println "Have read WorkerObject"
// extract worker process components
def wProcess = (WorkerInterface) workerObject.workerProcess
def inConnections =  workerObject.inConnections
def outConnections =  workerObject.outConnections
// create input channels
println "Creating in channels $inConnections"
def inChannels = new ChannelInputList()
for ( i in 0 ..< inConnections.size()){
	inChannels.append(NetChannel.numberedNet2One(inConnections[i]))
}
// signal that input channels have been created
hostRequest.write(new Signal())
println "Sent signal to host"
// wait for responding signal from host
loadChannel.read()
println "Response read from host - Creating out channels $outConnections"
// create the output channels
def outChannels = new ChannelOutputList()
for ( i in 0 ..< outConnections.size()){
	def outNodeAddr = new  TCPIPNodeAddress(outConnections[i][0], 1000)
	outChannels.append(NetChannel.any2net(outNodeAddr, outConnections[i][1]))
}
println "Created out channels"
// connect the channel lists to the worker process
wProcess.connect(inChannels, outChannels)
// create a process manager for wProcess
def wPM = new ProcessManager(wProcess)
println "Starting loaded worker process"
// start wProcess
def workerStartTime = timer.read()
wPM.start()
// and wait for it to terminate
wPM.join()
def workerEndTime = timer.read()
println "worker $nodeIP has terminated"
hostRequest.write([startTime, requestSentTime, workerReadTime, workerStartTime, workerEndTime])