package examples.c24.Distributed.hostNodeProcesses

import jcsp.userIO.*

def runId = Ask.string("HOST: what is the run identifier for timing purposes?  ")
def N = Ask.Int("What is the maximum number of words (N) in a sequence? (>=1) ", 1, 100)
def nodes = Ask.Int("How many worker nodes? (1,12) ", 1, 12)
def minSeqLen = Ask.Int("Minimum number of repetitions? (1 upwards) ", 1, 100)
def blockLength = Ask.Int("blocksize? (min 2*N, max 10000) ", 2*N, 10000)
println "Possible sources: test0  test  ACM  TMM  WaD  bible  2bibles  4bibles"
def nSources = Ask.Int ("Number of source texts? (1,10) ", 1, 10)
def sourceList = []
for ( s in 1..nSources){
  print " source $s "
  sourceList << Ask.string("FileName omitting extension? ")
}
println "Processing files: $sourceList"