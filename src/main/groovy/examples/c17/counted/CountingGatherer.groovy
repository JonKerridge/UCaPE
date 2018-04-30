package examples.c17.counted

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.*
import jcsp.lang.*


class CountingGatherer implements CSProcess {  
	
  def ChannelInput inChannel
  def ChannelOutput outChannel
  def ChannelOutput gatheredData
  def ChannelInput countInput
  
  void run(){
    def counter = 0
    def required = 0
    def gatherAlt = new ALT([countInput, inChannel])
	
    while (true){
      def index = gatherAlt.priSelect()
      if (index == 0) {
        required = countInput.read()
      }
      else {
        def v = inChannel.read()
        counter = counter + 1
        outChannel.write(v)
        if (counter == required) {
          println "Gathered value was ${v}"
          def cv = new CountedData ( counter: counter, value: v)
          gatheredData.write(cv)
        } // end if       
      } // end else
    } // end while    
  } // end run
}
