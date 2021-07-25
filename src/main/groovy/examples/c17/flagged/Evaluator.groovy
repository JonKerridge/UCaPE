package examples.c17.flagged

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovy_jcsp.*
import jcsp.lang.*


class Evaluator implements CSProcess {  
	
  ChannelInput inChannel
  
  void run() {
    while (true) {
      def v = inChannel.read()
      def ok = (v.c == (v.a +v.b))
      println "Evaluation: ${ok} from " + v.toString()
    }    
  }
}
