package examples.c22.collector

import jcsp.lang.*;
import examples.c22.universalClasses.*


class CopyOfCollector implements CSProcess {
	
	/*
	 * The Collector process uses a timer to determine how 
	 * long it takes to process all the input data using a 
	 * CSTimer.  The process reads a value from the fromWorkers 
	 * channel.  If it is the first such interaction the 
	 * timer is started.  If the value is an instance of a 
	 * terminating Sentinel then a check is made to see if 
	 * all the Base nodes have communicated such a Sentinel;  
	 * if so, the while loop is terminated, otherwise, the 
	 * timer is read and the resulting processed data object 
	 * is appended to the results list recording the time 
	 * the data was read.  This uses the abstract method 
	 * display that has been made concrete in TestObject.
	 * 
	 * Once the while loop has terminated the end time from 
	 * the timer is read and the results printed out in a 
	 * readable format.
	 */
	
	def fromWorkers
	def workers = 2
	
	void run(){
		def timer = new CSTimer()
		def terminated = 0
		def stopped = false
		def now = 0 
		def start 
		def first = true
		def results = []
		while (!stopped) {
			def o = fromWorkers.read()
			if (first) {
				start = timer.read()
				first = false
			}
			if (o instanceof Sentinel) {
				terminated = terminated + 1
				stopped = terminated == workers
			}
			else {
				now = timer.read()
				results << o.display(now)
			}			
		}
		def end = timer.read()
		def l = 1
		for ( line in results) {
			println "line: $l at \t$line"
			l = l + 1
		}
		println "elapsed time: ${end - start} msecs; processed ${results.size()} results"
	}
}
