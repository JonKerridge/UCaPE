package examples.c23.MontecarloPi

import jcsp.lang.*
import groovyJCSP.*

def workers = 2
def iterations = 19200000
def cores = 2

def E2W = Channel.one2oneArray(workers)
def W2E = Channel.one2oneArray(workers)

def toWorkers = new ChannelOutputList(E2W)
def fromWorkers = new ChannelInputList(W2E)

def testEmitter = new TestEmitter(toWorkers: toWorkers, workers: workers, iterations: iterations)
def testCollector = new TestCollector(fromWorkers: fromWorkers, workers: workers, iterations: iterations)
def w0 = new TestWorker(inChannel:E2W[0].in(), outChannel: W2E[0].out(), cores: cores)
def w1 = new TestWorker(inChannel:E2W[1].in(), outChannel: W2E[1].out(), cores: cores)

def network = [testEmitter, testCollector, w0, w1]
def timer = new CSTimer()
def startTime = timer.read()
new PAR(network).run()
def endTime = timer.read()
def elapsedTime = endTime - startTime
println "elapsedTime = $elapsedTime"