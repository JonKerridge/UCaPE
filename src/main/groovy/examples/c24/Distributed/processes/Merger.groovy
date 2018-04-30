package examples.c24.Distributed.processes

import examples.c24.Distributed.dataRecords.*
import groovyJCSP.ChannelInputList;
import jcsp.lang.*




class Merger implements CSProcess {
  
  def ChannelInputList fromWorkers
  def sourceList
  def runs
  def N
  def minSeqLen
  def outRoot = "C:\\Concordance\\OutputFiles\\Distributed\\OutFiles\\"
  def timeRoot = "C:\\Concordance\\OutputFiles\\Distributed\\Times\\"
  def runId = "SM"
  def seqLens = ["ACM":2, "TMM":2, "WAD":2, "bible":2, "2bibles":3, "4bibles":5]
  
  void run(){
    
    def allSentinels = { b ->
      def finished = false
      def ss = 0
      def len = b.size()
      while ( !finished && (b[ss] instanceof Sentinel ) ) {
        ss = ss + 1
        if ( ss == len) finished = true
      }
      return finished
    } // end allSentinels
    
    def minKey = { buffers ->
      def buffIds = []
      def minKey = Integer.MAX_VALUE
      buffers.each { b ->
        if  ((b instanceof PartConcordance) && (b.seqVal < minKey)) minKey = b.seqVal
      }
      def buffId = 0
      buffers.each { b ->
        if ( (b instanceof PartConcordance) && (b.seqVal == minKey) ) buffIds << buffId
        buffId = buffId + 1
      } 
      return buffIds     
    } // end minKey
    
    def timer = new CSTimer()
    def concordanceEntry = " "
    def nodes = fromWorkers.size()
    def timeFileName = timeRoot + runId + "_M_" + N + "_times.txt"
    def timeHandle = new File(timeFileName)
    if (timeHandle.exists()) timeHandle.delete()
    def timeWriter = timeHandle.newPrintWriter()
    for ( s in sourceList){
      for ( r in 1..runs){
        minSeqLen = seqLens[s]
        def startTime = timer.read()
        def fileName = outRoot + s + "_L_" + minSeqLen + "_N_" + n  +"_Dist.txt"
        def fileHandle = new File (fileName)
        if (fileHandle.exists()) fileHandle.delete()
        def fileWriter = fileHandle.newPrintWriter()
        def inputBuffers = []
        for ( n in 0..< nodes){
          inputBuffers << fromWorkers[n].read()
        }
        while ( ! allSentinels(inputBuffers)){
          def minBuffers = minKey(inputBuffers)
          def wordMap = [:]
          minBuffers.each{ minId ->
            def em = inputBuffers[minId].entryMap
            em.each {
              def key = it.key
              def value = it.value
              def currentEntry = wordMap.get(key, [])
              wordMap.put(key, (currentEntry + value).sort())
            }
          } // end of each minBuffers
          wordMap.each {  concordanceEntry = " "
            def keyWords = it.key
            def indexes = it.value
            def flatIndex = indexes.flatten()
            if (flatIndex.size() >= minSeqLen) {
                concordanceEntry = concordanceEntry + keyWords + ", "
                concordanceEntry = concordanceEntry + flatIndex.size() + ", "
                concordanceEntry = concordanceEntry + flatIndex.sort()
                fileWriter.println "$concordanceEntry"
            }
          } // end each wordMap entry
          minBuffers.each { minId ->
            inputBuffers[minId] = fromWorkers[minId].read()
          }            
        } // end while not all sentinels
        fileWriter.flush()
        fileWriter.close()
        def endTime = timer.read()
        println "MERGER, $s, $N, ${endTime - startTime} "
        timeWriter.println "MERGER, $s, $N, ${endTime - startTime} "
     } // end of for r
    } // end of for s
    timeWriter.flush()
    timeWriter.close()
  } // end run()
}
