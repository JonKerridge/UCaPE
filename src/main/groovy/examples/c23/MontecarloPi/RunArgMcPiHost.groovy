package examples.c23.MontecarloPi

import examples.c23.loaderObjects.*
import jcsp.net2.tcpip.TCPIPNodeAddress
import jcsp.net2.*
import jcsp.net2.mobile.*
import groovyJCSP.*
import jcsp.lang.*

def workers = new Integer(args[0]).intValue()
def cores = new Integer(args[1]).intValue()
// application specific arguments
def iterations = new Integer(args[2]).intValue()

def timer = new CSTimer()
def startTime = timer.read()
//def hostIP = "127.0.0.1"
//def hostAddr = new TCPIPNodeAddress(hostIP, 1000)
def hostAddr = new TCPIPNodeAddress(1000)
Node.getInstance().init(hostAddr)
def hostIP = hostAddr.getIpAddress()
println "Host running on $hostIP for $workers worker nodes with $cores cores each for $iterations iterations"
// create request channel
def hostRequest = NetChannel.numberedNet2One(1)
// read request from nodes
def loadChannels = new ChannelOutputList()
def nodes = []
for ( w in 1 .. workers ) {
	def workerRequest = (RequestWorker)hostRequest.read()
	def nodeLoadChannel = NetChannel.one2net(workerRequest.loadLocation, new CodeLoadingChannelFilter.FilterTX())
	loadChannels.append(nodeLoadChannel)
	nodes << workerRequest.nodeIP
}
println "Processed $workers worker requests"
def requestReadTime = timer.read()
// construct worker object for each node
def workerObjects = []
for ( w in 0..< workers) {
	workerObjects << new WorkerObject ( workerProcess: new McPiWorker(cores: cores),
										inConnections : [100],
										outConnections: [[hostIP, 100 + w]])
}

// send worker objects to each node
for ( w in 0 ..< workers ) {
	loadChannels[w].write(workerObjects[w])
}
def workersSentTime = timer.read()
println "Sent worker objects to workers"
// create in and out connection lists for Emitter and Collector
def emitterInConnections = []
def emitterOutConnections = []
for ( w in 0..< workers) {	
	emitterOutConnections << [nodes[w], 100]
}
def collectorInConnections = []
for ( w in 0..< workers) {
	collectorInConnections << (100 + w)
}
def collectorOutConnections = []
def emmiterInChannelList = new ChannelInputList()
def emitterOutChannelList = new ChannelOutputList()
def collectorInChannelList = new ChannelInputList()
def collectorOutChannelList = new ChannelOutputList()
// read a signal form each node to indicate input channels have been created
for ( w in 0 ..< workers ) {
	hostRequest.read()    
}
println "Read in channel creation complete signals from workers"

// construct inChannels for Emitter and Collector

emitterInConnections.each{ cn ->
	emmiterInChannelList.append(NetChannel.numberedNet2One(cn))
}

collectorInConnections.each{ cn ->
	collectorInChannelList.append(NetChannel.numberedNet2One(cn))
}

println "Sending signals to workers to create out channel connections"

// write a signal to each node to indicate all input channels have been created
for ( w in 0 ..< workers ) {
	loadChannels[w].write(new Signal())
}

// create out channel lists for Emitter and Collector

emitterOutConnections.each{ connection ->
	def outNodeAddr = new  TCPIPNodeAddress(connection[0], 1000)
	emitterOutChannelList.append(NetChannel.any2net(outNodeAddr, connection[1]))
}

collectorOutConnections.each{ connection ->
	def outNodeAddr = new  TCPIPNodeAddress(connection[0], 1000)
	collectorOutChannelList.append(NetChannel.any2net(outNodeAddr, connection[1]))
}


// construct local processes on host node
def emitter = new McPiEmitter(workers: workers, iterations: iterations)
emitter.connect(emmiterInChannelList, emitterOutChannelList)
def collector = new McPiCollector(workers: workers, iterations: iterations, cores: cores)
collector.connect(collectorInChannelList, collectorOutChannelList)
// create and start the local host processes
def emitterPM = new ProcessManager( emitter )
def collectorPM = new ProcessManager( collector)
def hostStartTime = timer.read()
emitterPM.start()
collectorPM.start()
println "started processes running on Host"

emitterPM.join()
collectorPM.join()
def hostEndTime = timer.read()
println "Host terminated"
def workerTimes = []
def hostStartup = requestReadTime - startTime
def hostLoad = workersSentTime - requestReadTime
def hostInitiate = hostStartTime - workersSentTime
def hostElapsed = hostEndTime - hostStartTime
workerTimes << ["Host", hostStartup, hostLoad, hostInitiate, hostElapsed]
for ( w in 0 ..< workers){
	def workerRawTimes = hostRequest.read()
	def startup = workerRawTimes[1] - workerRawTimes[0]
	def load = workerRawTimes[2] - workerRawTimes[1]
	def initiate = workerRawTimes[3] - workerRawTimes[2]
	def elapsed = workerRawTimes[4] - workerRawTimes[3]
	workerTimes << ["Wk: " + w, startup, load, initiate, elapsed]	
}
println "Node\tstart\tload\tinit\telapsed"
workerTimes.each { timings ->
	timings.each{ print "$it\t"}
	println""
}



