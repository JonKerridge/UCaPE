package examples.c24.Distributed.loadedProcesses

import examples.c24.Distributed.loaderObjects.*
import groovyJCSP.*
import examples.c24.Distributed.processes.*

class LoadedMerger implements WorkerInterface {
  
  def ChannelInputList inChannels
  def ChannelOutputList outChannels
  
  def sourceList
  def runs
  def N
  def minSeqLen
  def outRoot 
  def timeRoot
  def runId

  def connect(inChannels, outChannels){
    this.inChannels = inChannels
    this.outChannels = outChannels
  }  
  
  void run(){
    def merger = new Merger ( fromWorkers: inChannels,
                              N: N, 
                              sourceList: sourceList,
                              runs: runs,
                              minSeqLen: minSeqLen,
                              outRoot: outRoot,
                              timeRoot: timeRoot, 
                              runId: runId)
    new PAR([merger]).run()
  }

}
