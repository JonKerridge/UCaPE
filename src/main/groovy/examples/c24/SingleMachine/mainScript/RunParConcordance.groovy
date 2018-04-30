package examples.c24.SingleMachine.mainScript

import jcsp.lang.*;
import groovyJCSP.*;

import examples.c24.SingleMachine.methods.defs;
import examples.c24.SingleMachine.supportProcesses.*;


def timer = new CSTimer()
def drive = "C"
def inRoot = drive + ":\\Concordance\\SourceFiles\\"
def outRoot = drive + ":\\Concordance\\OutputFiles\\Parallel\\"
def N = 6
def minSeqLen = 0
def runs = 8
def timesFileName = outRoot + "Times" + N + minSeqLen + "_Par.txt"
def timesFile = new File(timesFileName)
if (timesFile.exists()) timesFile.delete()
def timesWriter = timesFile.newPrintWriter()
timesWriter.print "Par\tSource\tN\tminSeqLen\t"
timesWriter.println "Read\tGenerate\tEqualKeys\tConcordance\tTotal\tWords"
def seqLens = ["ACM":2, "TMM":2, "WAD":2, "bible":2, "2bibles":3, "4bibles":5]
for (source in ["ACM", "TMM", "WAD", "bible", "2bibles", "4bibles"]){
//for (source in ["ACM", "TMM", "WAD", "bible"]){
//for (source in ["test0"]){
  minSeqLen = seqLens[source]
  def fileName = inRoot + source + ".txt"
  def parOutFileName = []
  def parOutFile = []
  def parPrintWriter = []
  for ( n in 1..N){
    def parFileName = outRoot + source + N + minSeqLen + "_N_" + n + "_Par.txt"
	parOutFileName << parFileName
	def parFile = new File(parFileName)
	parOutFile << parFile
	if (parFile.exists()) parFile.delete()
	parPrintWriter << parFile.newPrintWriter()
	}	
	for (run in 1..runs){
      println "Processing: $fileName, N: $N, minSequenceLength: $minSeqLen"		
      def startTime = timer.read()
      def wordBuffer = new ArrayList(10000)
      def NSequenceLists = []
      for ( n in 1..N) NSequenceLists[n] = new ArrayList(10000)
      def fileHandle = new File (fileName)
      def fileReader = new FileReader(fileHandle)
      def wordCount = 0	
      fileReader.eachLine { line ->
        def words = defs.processLine(line)
        for ( w in words) {
          wordBuffer << defs.removePunctuation(w)
          NSequenceLists[1] << defs.charSum (wordBuffer[wordCount])
          wordCount = wordCount + 1
		}
      }
      def endRead = timer.read()
      def procList1 = (2..N).collect {n -> 
		return new parSequencer( n:n, inList: NSequenceLists[1], 
                                 outList: NSequenceLists[n])}			
      new PAR(procList1).run()
      def endGenSeq = timer.read()
      def equalKeyMapList = []
      for ( n in 1..N) equalKeyMapList[n] = [:]		
      def procList2 = (1..N).collect { n -> 
        return new parFindEqualKeys ( words: (wordCount - 1), startIndex: 0, 
                                      inList: NSequenceLists[n], 
                                      outMap: equalKeyMapList[n])}		
      new PAR(procList2).run()
      def endFindEqualKeys = timer.read()
      def procList3 = (1..N).collect { n -> 
        return new parExtractConcordance ( equalMap: equalKeyMapList[n], n: n, 
                                           startIndex: 0, words: wordBuffer, 
                                           minSeqLen: minSeqLen,
                                           printWriter: parPrintWriter[n-1])}	
      new PAR(procList3).run()	
      def endConcordance = timer.read()
      def readTime = endRead - startTime
      def genTime = endGenSeq - endRead
      def equalKeysTime = endFindEqualKeys - endGenSeq
      def concordanceTime = endConcordance - endFindEqualKeys
      def totalTime = endConcordance - startTime		
      timesWriter.print "$run\t$source\t$N\t$minSeqLen\t\t"
      timesWriter.println "$readTime\t$genTime\t\t$equalKeysTime"  + 
                          "\t\t$concordanceTime\t\t$totalTime\t$wordCount"		
      print "Par\tSource\tN\tminSeqLen\t"
      println "Read\tGenerate\tEqualKeys\tConcordance\tTotal\tWords"
      print "$run\t$source\t$N\t$minSeqLen\t\t"
      println"$readTime\t$genTime\t\t$equalKeysTime" + 
             "\t\t$concordanceTime\t\t$totalTime\t$wordCount"
  }
  println ""
  timesWriter.println "\n\n\n"
  timesWriter.flush()
}
timesWriter.close()

