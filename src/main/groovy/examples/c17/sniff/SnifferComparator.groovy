package examples.c17.sniff

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovy_jcsp.*
 

class SnifferComparator implements CSProcess {  
	
  ChannelInput fromCopy
  ChannelInput fromScaler
  long interval = 10000
  
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
