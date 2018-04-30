package examples.c22.collector

import jcsp.lang.*;
import examples.c22.universalClasses.*





class Collector implements CSProcess {
		
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
