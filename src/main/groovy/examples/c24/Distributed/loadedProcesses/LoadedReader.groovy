package examples.c24.Distributed.loadedProcesses

import examples.c24.Distributed.loaderObjects.*
import groovy_jcsp.*
import examples.c24.Distributed.processes.*

class LoadedReader implements WorkerInterface {
  
  ChannelInputList inChannels
  ChannelOutputList outChannels

  def inRoot 
  def N 
  def blockLength 
  def runs 
  def sourceList
  def timeRoot
  def runId

  def connect(inChannels, outChannels){
    this.inChannels = inChannels
    this.outChannels = outChannels
  }  
  
  void run(){
    def reader = new Reader( N: N,
                             blockLength: blockLength,
                             outChannels: outChannels,
                             sourceList: sourceList,
                             runs: runs,
                             inRoot: inRoot,
                             timeRoot: timeRoot, runId: runId)
    new PAR([reader]).run()
  }

}
