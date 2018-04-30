package examples.c24.Distributed.hostNodeProcesses

import jcsp.net2.tcpip.TCPIPNodeAddress
import jcsp.net2.*
import jcsp.net2.mobile.*
import examples.c24.Distributed.loadedProcesses.*
import examples.c24.Distributed.loaderObjects.*
import groovyJCSP.*
import jcsp.lang.*
import jcsp.userIO.*

def drive = "C"
def inRoot = drive + ":\\ConcordanceSources\\"
def outRoot = drive + ":\\ConcordanceOutput\\Distributed\\"
def timeRoot = drive + ":\\ConcordanceOutput\\Times\\"

/*
def N = 6   // this is the number of merger nodes required
def nodes = 2
def sourceList = ["bible", "2bibles", "2bibles"]
def runs = 2
def minSeqLen = 2
def blockLength = 5000
def runId = "runTimes"
*/
// interactive data input
def runId = Ask.string("HOST: what is the run identifier for timing purposes?  ")
def N = Ask.Int("What is the maximum number of words (N) in a sequence? (>=1) ", 1, 100)
def nodes = Ask.Int("How many worker nodes? (1,12) ", 1, 12)
def minSeqLen = Ask.Int("Minimum number of seqquence repetitions? (1 upwards) ", 1, 100)
def blockLength = Ask.Int("blocksize? (min 2*N, max 10000) ", 2*N, 10000)
println "Possible sources: test0  test  ACM  TMM  WaD  bible  2bibles  4bibles"
def nSources = Ask.Int ("Number of source texts? (1,10) ", 1, 10)
def sourceList = []
for ( s in 1..nSources){
  print " source $s "
  sourceList << Ask.string("FileName omitting extension? ")
}
def runs = Ask.Int("How many runs per text (>=1) ", 1, 100)
println "Processing files: $sourceList, Nodes: $nodes, N:$N, runs: $runs, blockLength: $blockLength called: $runId"

// end of interactive input

def timer = new CSTimer()
def startTime = timer.read()
//def hostIP = "127.0.0.1"
//def hostAddr = new TCPIPNodeAddress(hostIP, 1000)

def hostAddr = new TCPIPNodeAddress(1000)
Node.getInstance().init(hostAddr)
def totalNodes = N + nodes
println "Host running on ${hostAddr.getIpAddress()} for $totalNodes merger and worker nodes"
// create request channel
def hostRequest = NetChannel.numberedNet2One(1)
// read request from nodes
def nodeLoadChannels = new ChannelOutputList()
def mergerLoadChannels = new ChannelOutputList()
def workerNodes = []
for ( w in 1 .. nodes ) {
    def workerRequest = (RequestWorker)hostRequest.read()
    def nodeLoadChannel = NetChannel.one2net( workerRequest.loadLocation, 
                                              new CodeLoadingChannelFilter.FilterTX())
    nodeLoadChannels.append(nodeLoadChannel)
    workerNodes << workerRequest.nodeIP
}

def mergerNodes = []
for ( m in 1 .. N ) {
    def workerRequest = (RequestWorker)hostRequest.read()
    def nodeLoadChannel = NetChannel.one2net( workerRequest.loadLocation,
                                              new CodeLoadingChannelFilter.FilterTX())
    mergerLoadChannels.append(nodeLoadChannel)
    mergerNodes << workerRequest.nodeIP
}

def requestReadTime = timer.read()
println "Have read requests from $totalNodes"
def workerOutConnections = []
def nc = 100
for (n in 0..<nodes) {
  def nodeOutConnections = []
  for ( m in 0..<N){
    nodeOutConnections << [mergerNodes[m], nc]
  }
  workerOutConnections << nodeOutConnections
  nc = nc + 1
}
def mergerInConnections = []
for ( n in 0..<nodes) mergerInConnections << 100 + n
println "Worker Out Connections"
workerOutConnections.each{println "$it"}
println "Merger In Connections"
mergerInConnections.each{println "$it"}
// construct worker object for each node
def workerObjects = []  // stored in order workerNodes followed by N mergerNodes
for ( w in 1 .. nodes) {
    workerObjects << new WorkerObject ( workerProcess: new LoadedNode(N:N, runs: runs, node: w,
                                                                      sourceList: sourceList,
                                                                      timeRoot: timeRoot, runId: runId),
                                        inConnections : [99],
                                        outConnections: workerOutConnections[w-1])
}

def mergerObjects = []
for ( m in 1 .. N) {
    mergerObjects << new WorkerObject ( workerProcess: new LoadedMerger(N:m, runs: runs,
                                                                        sourceList: sourceList,
                                                                        minSeqLen: minSeqLen,
                                                                        outRoot:outRoot,
                                                                        timeRoot: timeRoot, runId: runId),
                                        inConnections : mergerInConnections,
                                        outConnections: [])
}

// send worker objects to each node  assuming all have a disc
println "sending workers objects to $totalNodes workers"
for ( w in 0 ..< nodes ) {
    nodeLoadChannels[w].write(workerObjects[w])
}

for ( m in 0 ..< N ) {
    mergerLoadChannels[m].write(mergerObjects[m])
}
def workersSentTime = timer.read()
println "Sent worker objects to workers"

// create out connection list for Reader
def readerInConnections = []
def readerOutConnections = []
for ( w in 0..< nodes) {
    readerOutConnections << [workerNodes[w], 99]
}
def readerInChannelList = new ChannelInputList()
def readerOutChannelList = new ChannelOutputList()
// read a signal form each node to indicate input channel have been created
for ( w in 0 ..< totalNodes ) {
    hostRequest.read()
}
println "Read the channel creation complete signals from other nodes"

println "Sending signals to workers to create out channel connections"

// write a signal to each node to indicate all input channels have been created
for ( w in 0 ..< nodes ) {
  nodeLoadChannels[w].write(new Signal())
}

for ( m in 0 ..< N ) {
  mergerLoadChannels[m].write(new Signal())
}


// create out channel lists for reader and Collector
println " readerOutConnections: $readerOutConnections"

//for ( i in 0 ..< readerOutConnections.size()){
readerOutConnections.each{ connection ->
    def outNodeAddr = new  TCPIPNodeAddress(connection[0], 1000)
    readerOutChannelList.append(NetChannel.any2net(outNodeAddr, connection[1]))
}



// construct local processes on host node
def reader = new LoadedReader(N: N, blockLength: blockLength, runs: runs,
                              inRoot: inRoot,
                              sourceList: sourceList,
                              timeRoot: timeRoot, runId: runId)
reader.connect(readerInChannelList, readerOutChannelList)
// create and start the local host processes
def readerPM = new ProcessManager( reader )
def hostStartTime = timer.read()
readerPM.start()
println "started process running on Host"

readerPM.join()
def hostEndTime = timer.read()
println "Host terminated"
def workerTimes = []
def hostStartup = requestReadTime - startTime
def hostLoad = workersSentTime - requestReadTime
def hostInitiate = hostStartTime - workersSentTime
def hostElapsed = hostEndTime - hostStartTime
workerTimes << ["Host", hostStartup, hostLoad, hostInitiate, hostElapsed]
for ( w in 0 ..< totalNodes){
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

