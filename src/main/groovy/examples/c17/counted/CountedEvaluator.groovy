package examples.c17.counted

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import groovyJCSP.*
import jcsp.lang.*

class CountedEvaluator implements CSProcess {  
	
  def ChannelInput inChannel
  
  void run() {
    while (true) {
      def v = inChannel.read()
      //def ok = ( (v.value % 2) == 0 )
      def ok = ( ( v.value / (v.counter - 1) ) == 2 )
      println "Evaluation: ${ok} from " + v.toString()
    }    
  }
}
