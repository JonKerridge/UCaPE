package examples.c23.MontecarloPi
import examples.c23.loaderObjects.RequestWorker;
import examples.c23.loaderObjects.Signal;
import examples.c23.loaderObjects.WorkerObject;

import jcsp.net2.tcpip.TCPIPNodeAddress
import jcsp.net2.*
import jcsp.net2.mobile.*
import examples.c23.*
import groovyJCSP.*
import jcsp.lang.*
import jcsp.userIO.*

/*
def iterations = args[0]
def workers = args[1]
def cores = args[2]
*/


def iterations = 19200000
def workers = 2
def cores = 1

println "Running host with iterations = $iterations, $workers workers and $cores cores each"
def timer = new CSTimer()
def startTime = timer.read()
def hostIP = "127.0.0.1"
def hostAddr = new TCPIPNodeAddress(hostIP, 1000)
//def hostAddr = new TCPIPNodeAddress(1000)
Node.getInstance().init(hostAddr)
println "Host running on ${hostAddr.getIpAddress()} for $workers worker nodes"
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
def requestReadTime = timer.read()
println "Have read requests from $nodes"
// construct worker object for each node
def workerObjects = []
for ( w in 0..< workers) {
	workerObjects << new WorkerObject ( workerProcess: new McPiWorker(cores: cores),
										inConnections : [100],
										outConnections: [[hostIP, 100 + w]])
}

// send worker objects to each node
println "sending workers objects to $workers workers"
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
// read a signal form each node to indicate input channel have been created
for ( w in 0 ..< workers ) {
	hostRequest.read()    
}
println "Read in channel creation complete signals from workers"
// construct inChannels for Emitter and Collector
//for ( i in 0 ..< emitterInConnections.size()){
emitterInConnections.each{ cn ->
	emmiterInChannelList.append(NetChannel.numberedNet2One(cn))
}
//for ( i in 0 ..< collectorInConnections.size()){
collectorInConnections.each{ cn ->
	collectorInChannelList.append(NetChannel.numberedNet2One(cn))
}

println "Sending signals to workers to create out channel connections"

// write a signal to each node to indicate all input channels have been created
for ( w in 0 ..< workers ) {
	loadChannels[w].write(new Signal())
}

// create out channel lists for Emitter and Collector
println " emitterOutConnections: $emitterOutConnections"
println " collectorOutConnections: $collectorOutConnections"

//for ( i in 0 ..< emitterOutConnections.size()){
emitterOutConnections.each{ connection ->
	def outNodeAddr = new  TCPIPNodeAddress(connection[0], 1000)
	emitterOutChannelList.append(NetChannel.any2net(outNodeAddr, connection[1]))
}

//for ( i in 0 ..< collectorOutConnections.size()){
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



