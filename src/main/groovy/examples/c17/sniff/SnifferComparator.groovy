package examples.c17.sniff

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
 

class SnifferComparator implements CSProcess {  
	
  def ChannelInput fromCopy
  def ChannelInput fromScaler
  def interval = 10000
  
  void run() {
    def connect = Channel.one2one()  
	  
    def testList = [ new Sniffer ( fromSystemCopy: fromCopy,
                                    toComparator: connect.out(),
                                    sampleInterval: interval), 
								
                     new Comparator ( fromSystemOutput: fromScaler,
                                       fromSniffer: connect.in() )
                    ]
    new PAR(testList).run()
  }
}
