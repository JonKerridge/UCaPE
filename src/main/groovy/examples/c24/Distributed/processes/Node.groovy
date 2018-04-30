package examples.c24.Distributed.processes

import groovyJCSP.*
import jcsp.lang.*





class Node implements CSProcess {
  
  def ChannelInput nodeInChannel
  def ChannelOutputList toMergers // N of these
  def N = 0
  def sourceList
  def runs
  def node
  def timeWriter

  void run(){  
    def timer = new CSTimer()
    def startSortPhase = Channel.one2any()
    for ( s in sourceList){
      for ( r in 1 .. runs){
        def startTime = timer.read()
        def sequenceBlockList = [] // holds each of the sequence blocks
        def worker = new Worker( N: N, inChannel: nodeInChannel, 
                                  ssp: startSortPhase.out(),
                                  sbl: sequenceBlockList, source: s, 
                                  run: r, node:node,
                                  timeWriter: timeWriter )
        def sorters = (1..N).collect{sn ->
             new Sorter(Nvalue: sn,
                        startChannel: startSortPhase.in(),
                        toMerger: toMergers[sn-1],
                        sbl: sequenceBlockList,
                        source: s,
                        run: r,
                        node: node,
                        timeWriter: timeWriter )
        }
        def network = sorters + worker
        new PAR(network).run()
        def endTime = timer.read()
        println "NODE, $node, $s, $r, ${endTime - startTime}"
        timeWriter.println "NODE, $node, $s, $r, ${endTime - startTime}"
      } // end runs
    } // end sources
    timeWriter.flush()
    timeWriter.close()
  } // end run()
}

