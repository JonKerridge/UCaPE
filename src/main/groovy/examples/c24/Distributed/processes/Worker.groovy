package examples.c24.Distributed.processes

import examples.c24.Distributed.dataRecords.*
import groovyJCSP.*
import jcsp.lang.*
import examples.c24.SingleMachine.supportProcesses.*
import examples.c24.SingleMachine.methods.*


class Worker implements CSProcess {
  
  def N = 0
  def sbl   // the reference to the sequenceBlockList
  def source
  def run
  def ChannelInput inChannel
  def ChannelOutput ssp // connection to startSortPhase
  def node
  def timeWriter

  void run(){
   def timer = new CSTimer()
   def endTime
    def timesList = []
    for (t in 0..< 4) timesList[t] = 0
    def o = inChannel.read()
    def beginTime = timer.read()
    while ( ! (o instanceof Sentinel)) {
      def startTime = timer.read()
      def startSub = o.startSubscript
      def punctuatedWords = o.words
      def lastBlock = o.last
      def NSequenceLists = []
      def wordCount = 0
      for ( n in 0..N) NSequenceLists[n] = new ArrayList(1000)
      def wordBuffer = new ArrayList(punctuatedWords.size())
      for ( w in punctuatedWords){
        wordBuffer << defs.removePunctuation(w)
        NSequenceLists[0] << defs.charSum (wordBuffer[wordCount])
        wordCount = wordCount + 1
      } // end for punctuatedWords
      def endRemove = timer.read()
      timesList[0] = timesList[0] + endRemove - startTime      
      def procList1 = (1..N).collect {n ->
        return new parMultiSequencer( Nmax: N, n:n, 
                                      baseList: NSequenceLists[0],
                                      outList: NSequenceLists[n], 
                                      lastBlock: lastBlock)
        }
      new PAR(procList1).run()
      def endSequencer = timer.read()
      timesList[1] = timesList[1] + endSequencer - endRemove      
      def equalKeyMapList = []
      for ( n in 1..N) equalKeyMapList[n] = [:]     
      def procList2 = (1..N).collect { n ->
        return new parFindEqualKeys ( words: (wordCount - 1), 
                                      startIndex: startSub,
                                      inList: NSequenceLists[n],
                                      outMap: equalKeyMapList[n])
        }
      new PAR(procList2).run()
      def endEqualKeys = timer.read()
      timesList[2] = timesList[2] + endEqualKeys - endSequencer       
      def equalWordMapList = []
      for ( n in 1..N) equalWordMapList[n] = [:]
      def procList3 = (1..N).collect { n -> 
        return new parExtractUniqueSequences ( 
            equalMap: equalKeyMapList[n], n: n, 
            startIndex: startSub, words: wordBuffer, 
            equalWordMap: equalWordMapList[n] )
        }   
      new PAR(procList3).run()  
      sbl << new SequenceBlock (startSubscript: startSub,
                                words: wordBuffer,
                                NSequenceLists: NSequenceLists,
                                equalKeyMapList: equalKeyMapList,
                                equalWordMapList: equalWordMapList)
      o = inChannel.read()
      endTime = timer.read()
      timesList[3] = timesList[3] + endTime - endEqualKeys        
   } // end while not Sentinel
    println "WORKER, $source, $run, ${timesList[0]}, " +
            "${timesList[1]}, ${timesList[2]}, ${timesList[3]}, "+
            "${endTime - beginTime}"
    timeWriter.println "WORKER, $source, $run, ${timesList[0]}, "+
            "${timesList[1]}, ${timesList[2]}, ${timesList[3]}, "+
            "${endTime - beginTime}"
    for ( n in 1..N) ssp.write(new Sentinel())
  } // end run()
}
