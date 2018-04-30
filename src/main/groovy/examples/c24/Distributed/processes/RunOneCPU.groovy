package examples.c24.Distributed.processes

import groovyJCSP.*
import jcsp.lang.*

def nodes = 1
def runs = 1
def N = 6
def minSeqLength = 0

def sourceList = ["ACM", "TMM", "WAD", "bible", "2bibles", "4bibles"]
def readerToWorker = Channel.one2oneArray(nodes)
def toWorkers = new ChannelOutputList(readerToWorker)

def nodeToMergerList = []
for ( n in 1 .. nodes) nodeToMergerList << Channel.one2anyArray(N)
def sortersToMergersList = []
for ( n in 1 .. nodes) sortersToMergersList << new ChannelOutputList(nodeToMergerList[n-1])

def inputsToMergers = []

for ( m in 1..N){
  def toMerger = new ChannelInputList()
  for ( n in 1 .. nodes) toMerger.append (nodeToMergerList[n-1][m-1].in())
  inputsToMergers << toMerger
}


def reader = new Reader(N: N, blockLength: 5000, outChannels: toWorkers, sourceList: sourceList, runs: runs)

def workers = (0..<nodes).collect {n ->
                new groovy.util.Node( nodeInChannel: readerToWorker[n].in(),
                          node: n, sourceList: sourceList, runs:runs, N: N ,
                          toMergers: sortersToMergersList[n-1])
              }
def mergers = (1 .. N).collect {m ->
                new Merger(sourceList: sourceList, runs:runs, N: m ,
                           minSeqLen: minSeqLength,
                           fromWorkers: inputsToMergers[m-1])
                }
def network = workers + reader + mergers

new PAR(network).run()
