package examples.c23.MontecarloPi

import jcsp.lang.*
import groovyJCSP.*

def workers = 2
def iterations = 19200000
def cores = 2

def E2W = Channel.one2oneArray(workers)
def W2C = Channel.one2oneArray(workers)

def toWorkers = new ChannelOutputList(E2W)
def fromWorkers = new ChannelInputList(W2C)

def emitter = new McPiEmitter(workers: workers, iterations: iterations)
emitter.connect(null, toWorkers)

def collector = new McPiCollector(workers: workers, iterations: iterations)
collector.connect(fromWorkers, null)

def w0 = new McPiWorker(cores: cores)
def w0InChannels = new ChannelInputList()
def w0OutChannels = new ChannelOutputList()

w0InChannels.append(E2W[0].in())
w0OutChannels.append(W2C[0].out())
w0.connect(w0InChannels, w0OutChannels)

def w1 = new McPiWorker(cores: cores)
def w1InChannels = new ChannelInputList()
def w1OutChannels = new ChannelOutputList()
w1InChannels.append(E2W[1].in())
w1OutChannels.append(W2C[1].out())
w1.connect(w1InChannels, w1OutChannels)


def network = [emitter, collector, w0, w1]
def timer = new CSTimer()
def startTime = timer.read()
new PAR(network).run()
def endTime = timer.read()
def elapsedTime = endTime - startTime
println "elapsedTime = $elapsedTime"