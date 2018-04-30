package sanityCheck
    
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput

class Consumer implements CSProcess {
  
  def ChannelInput inChannel
  def consumeList = []
  
  void run() {
    def i = 1000
    println "Consumer expecting 10 integers"
    while ( i > 0 ) {
      i = inChannel.read()
      println "Read : ${i}"
      if (i > 0 )consumeList << i
    }
    println "Consumer has finished"
    println "\n\nInstallation Sanity Check has succeeded"
  }
}

