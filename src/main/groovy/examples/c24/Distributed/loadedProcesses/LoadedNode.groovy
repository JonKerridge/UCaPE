package examples.c24.Distributed.loadedProcesses

import examples.c24.Distributed.loaderObjects.*
import groovyJCSP.*
import examples.c24.Distributed.processes.*

class LoadedNode implements WorkerInterface {
  
  def ChannelInputList inChannels       // one input channel from Reader
  def ChannelOutputList outChannels     // N output channels to Mergers
 
  def N = 0
  def sourceList
  def runs
  def node
  def timeRoot = "C:\\Concordance\\OutputFiles\\Distributed\\Times\\"
  def runId  = "SM"

  def connect(inChannels, outChannels){
    this.inChannels = inChannels
    this.outChannels = outChannels
  }  
  
  void run(){
    def timeFileName = timeRoot + runId + "_N_" + node + "_times.txt"
    def timeHandle = new File(timeFileName)
    if (timeHandle.exists()) timeHandle.delete()
    def timeWriter = timeHandle.newPrintWriter()
    def node = new Node( nodeInChannel: inChannels[0],
                         node: node,
                         sourceList: sourceList,
                         runs: runs,
                         N: N,
                         toMergers: outChannels,
                         timeWriter: timeWriter)
    new PAR([node]).run()
  }

}
