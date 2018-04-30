package examples.c24.Distributed.processes

import jcsp.lang.*
import examples.c24.Distributed.dataRecords.*





class Sorter implements CSProcess {
  
  def Nvalue = 0
  def ChannelInput startChannel
  def ChannelOutput toMerger
  def sbl   // the reference to the sequenceBlockList
  def source
  def run
  def node 
  def timeWriter

  void run(){ 
    
    def union = {s1, s2 ->
      s2.each{v ->
        if ( !(s1.contains(v))) s1 << v
      }
    } // end union
    
    def timer = new CSTimer()
    def sbKeys = []
	  startChannel.read()
    def startTime = timer.read()
	  sbl.each{ sb ->
      def ewmN = sb.equalWordMapList[Nvalue]
      def mapKeys = ewmN.keySet()
      mapKeys.each { mk ->
        union (sbKeys, mk)
      }
    }
    def sortedKeys = sbKeys.sort()
    def sortedTime = timer.read()
    sortedKeys.each { keySV ->
      def compositeWordSSMap = [:]
      sbl.each { sb -> 
        def wmEntry = sb.equalWordMapList[Nvalue].get(keySV, [] )
        wmEntry.each { 
          def wordKey = it.key
          def subScripts = it.value
          def existingSS = compositeWordSSMap.get(wordKey, [])
          existingSS << subScripts
          compositeWordSSMap.put(wordKey, existingSS)          
        } // end of each wmEntry
      }// end of each sbl
      def partConcordance = new PartConcordance( seqVal: keySV,
                                                 entryMap: compositeWordSSMap)
      toMerger.write(partConcordance)
    } // end of each sbKeys
    def endTime = timer.read()
    toMerger.write(new Sentinel())
    println "SORTER, $source, $run, $node, $Nvalue, ${sortedTime - startTime}, " +
            "${endTime - sortedTime}, ${endTime - startTime}"
    timeWriter.println "SORTER, $source, $run, $node, $Nvalue, " +
                       "${sortedTime - startTime}, ${endTime - sortedTime}, " +
                       "${endTime - startTime}"
  } // end of run()
}
