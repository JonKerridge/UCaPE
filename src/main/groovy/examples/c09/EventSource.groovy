package examples.c09
  
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*

class EventSource implements CSProcess {
	
  def source 
  def iterations = 99
  def minTime = 100
  def maxTime = 250
  ChannelOutput outChannel
   
  void run() {
    def eg2h = Channel.one2one()    
    def sourceList = [ new EventGenerator ( source: source,
                                            initialValue: 100 * source,
                                            iterations: iterations,
                                            minTime: minTime,
                                            maxTime: maxTime,
                                            outChannel: eg2h.out()),
                       new EventHandler ( inChannel: eg2h.in(),
                                          outChannel: outChannel)
                      ]
	
    new PAR (sourceList).run()
  }
}
