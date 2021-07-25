package examples.c23.MontecarloPi

import jcsp.lang.*
import groovy_jcsp.*

class TestCollector implements CSProcess {
	
	ChannelInputList fromWorkers
	def workers = 1
	def iterations = 192
	
	void run(){
		println "running testCollector"
		def collector = new McPiCollector ( workers: workers,
											iterations: iterations)
		collector.connect(fromWorkers, null)
		new PAR ([collector]).run()
	}

}
