package examples.c23
import examples.c23.loaderObjects.RequestWorker;
import examples.c23.loaderObjects.Signal;
import examples.c23.loaderObjects.WorkerObject;

import jcsp.net2.tcpip.TCPIPNodeAddress
import jcsp.net2.*
import jcsp.net2.mobile.*
//import examples.23.*
import groovyJCSP.*
import jcsp.lang.*
import jcsp.userIO.*

def workers = 4
def timer = new CSTimer()
def startTime = timer.read()
def hostIP = "127.0.0.1"
def hostAddr = new TCPIPNodeAddress(hostIP, 1000)
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
workerObjects << new WorkerObject ( workerProcess: new Sprocess(),
									inConnections : [100, 101, 102],
									outConnections: [[nodes[1], 100], [nodes[2], 100]])

workerObjects << new WorkerObject ( workerProcess: new WProcess(),
	                                  inConnections: [100],
								      outConnections:  [ [nodes[0], 101], [nodes[3], 100] ])

workerObjects << new WorkerObject ( workerProcess: new WProcess( modifier: 200),
									  inConnections: [100],
									  outConnections:  [ [nodes[0], 102], [nodes[3], 101] ])

workerObjects << new WorkerObject ( workerProcess: new Mprocess(),
									inConnections : [100, 101],
									outConnections: [[hostIP, 100]])


// send worker objects to each node
for ( w in 0 ..< workers ) {
	loadChannels[w].write(workerObjects[w])
}
def workersSentTime = timer.read()
println "Sent worker objects to workers"
// create in and out connection lists for Emitter and Collector
def emitterInConnections = []
def emitterOutConnections = [[nodes[0], 100]]
def collectorInConnections = [100]
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
for ( i in 0 ..< emitterInConnections.size()){
	emmiterInChannelList.append(NetChannel.numberedNet2One(emitterInConnections[i]))
}
for ( i in 0 ..< collectorInConnections.size()){
	collectorInChannelList.append(NetChannel.numberedNet2One(collectorInConnections[i]))
}

println "Sending signals to workers to create out channel connections"

// write a signal to each node to indicate all input channels have been created
for ( w in 0 ..< workers ) {
	loadChannels[w].write(new Signal())
}

// create out channel lists for Emitter and Collector

for ( i in 0 ..< emitterOutConnections.size()){
	def outNodeAddr = new  TCPIPNodeAddress(emitterOutConnections[i][0], 1000)
	emitterOutChannelList.append(NetChannel.any2net(outNodeAddr, emitterOutConnections[i][1]))
}

for ( i in 0 ..< collectorOutConnections.size()){
	def outNodeAddr = new  TCPIPNodeAddress(collectorOutConnections[i][0], 1000)
	collectorOutChannelList.append(NetChannel.any2net(outNodeAddr, collectorOutConnections[i][1]))
}


// construct local processes on host node
def emitter = new Emitter()
emitter.connect(emmiterInChannelList, emitterOutChannelList)
def collector = new Collector()
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
println "Node\tstart\tload\tbegin\telapsed"
workerTimes.each { timings ->
	timings.each{ print "$it\t"}
	println""
}



