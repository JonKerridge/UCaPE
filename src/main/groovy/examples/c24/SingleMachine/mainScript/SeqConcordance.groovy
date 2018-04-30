package examples.c24.SingleMachine.mainScript

import jcsp.lang.*;
import examples.c24.SingleMachine.methods.defs;

def timer = new CSTimer()
def drive = "C"
def inRoot = drive + ":\\Concordance\\SourceFiles\\"
def outRoot = drive + ":\\Concordance\\OutputFiles\\Sequential\\"
def N = 6
def minSeqLen = 0
def runs = 8
def timesFileName = outRoot + "Times" + N + minSeqLen + "_Seq.txt"
def timesFile = new File(timesFileName)
if (timesFile.exists()) timesFile.delete()
def timesWriter = timesFile.newPrintWriter()
timesWriter.print "Seq\tSource\tN\tminSeqLen\t"
timesWriter.println "Read\tGenerate\tEqualKeys\tConcordance\tTotal\tWords"
def seqLens = ["ACM":2, "TMM":2, "WAD":2, "bible":2, "2bibles":3, "4bibles":5]
for (source in ["ACM", "TMM", "WAD", "bible", "2bibles", "4bibles"]){
//for (source in ["test0"]){
	def fileName = inRoot + source + ".txt"
  minSeqLen = seqLens[source]
	def outFileName = outRoot + source + N + minSeqLen + "_Seq.txt"
	def outFile = new File(outFileName)
	if (outFile.exists()) outFile.delete()
	def printWriter = outFile.newPrintWriter()
	for (run in 1..runs){
		println "Processing: $fileName, N: $N, minSequenceLength: $minSeqLen"		
		def startTime = timer.read()
		def wordBuffer = new ArrayList(10000)
		def NSequenceLists = []
		for ( n in 1..N) NSequenceLists[n] = new ArrayList(10000)
		def fileHandle = new File (fileName)
		def fileReader = new FileReader(fileHandle)
		def wordCount = 0	// number of words in file
		fileReader.eachLine { line ->
			//println "input: $line"
			def words = defs.processLine(line)
			for ( w in words) {
				wordBuffer << defs.removePunctuation(w)
				NSequenceLists[1] << defs.charSum (wordBuffer[wordCount])
				wordCount = wordCount + 1
			}
		}
		def endRead = timer.read()
		for ( n in 2..N) defs.sequencer ( n, NSequenceLists[1], NSequenceLists[n])
		def endGenSeq = timer.read()
		def equalKeyMapList = []
		for ( n in 1..N) {
			equalKeyMapList[n] = new TreeMap()
			defs.extractEqualValues ((wordCount - 1), 0, NSequenceLists[n], equalKeyMapList[n])
		}
		def endFindEqualKeys = timer.read()
		for ( n in 1..N){
			defs.extractConcordance (equalKeyMapList[n], n, 0, wordBuffer, minSeqLen, printWriter )
		}		
		def endConcordance = timer.read()
		def readTime = endRead - startTime
		def genTime = endGenSeq - endRead
		def equalKeysTime = endFindEqualKeys - endGenSeq
		def concordanceTime = endConcordance - endFindEqualKeys
		def totalTime = endConcordance - startTime
		
		timesWriter.print "$run\t$source\t$N\t$minSeqLen\t\t"
		timesWriter.println "$readTime\t$genTime\t\t$equalKeysTime\t\t$concordanceTime\t\t$totalTime\t$wordCount"
		
		print "Seq\tSource\tN\tminSeqLen\t"
		println "Read\tGenerate\tEqualKeys\tConcordance\tTotal\tWords"
		print "$run\t$source\t$N\t$minSeqLen\t\t"
		println"$readTime\t$genTime\t\t$equalKeysTime\t\t$concordanceTime\t\t$totalTime\t$wordCount"
		printWriter.flush()
		printWriter.close()
	}
	println ""
	timesWriter.println "\n\n\n"
	timesWriter.flush()
}

timesWriter.close()
