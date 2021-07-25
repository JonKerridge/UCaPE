package examples.c14
 
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

class TargetFlusher implements CSProcess {
	
  def buckets
  ChannelOutput targetsFlushed
  ChannelInput flushNextBucket
  def Barrier initBarrier

  void run() {
    def nBuckets = buckets.size()
    def currentBucket = 0
    def targetsInBucket = 0
    while (true) {
      flushNextBucket.read()
      targetsInBucket = buckets[currentBucket].holding()
      while ( targetsInBucket == 0) {
        currentBucket = (currentBucket + 1) % nBuckets
        targetsInBucket = buckets[currentBucket].holding()        
      } // end of while targetsInBucket
      initBarrier.reset( targetsInBucket)
      targetsFlushed.write(targetsInBucket)
      buckets[currentBucket].flush()
      currentBucket = (currentBucket + 1) % nBuckets
    } // end of while true
  }
}
