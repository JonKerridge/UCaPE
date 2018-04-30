package examples.c22.emitter;



class CopyOfTestObject implements ManipulateInterface {
	
	def workerId = -1
	def sum = 0
	def data = []
	def dataSize = 0
	
	def TestObject (elements, m) {
		for ( i in 0..<elements) data[i] = (i * (m+1)) + 1
		dataSize = elements
	}
	
	def manipulate (x){
		for ( i in 0..<dataSize) data[i] = data[i] * (x + 1)
		for ( i in 0..<dataSize) sum = sum + data[i]
		workerId = x
	}
	
	def String display (now){
		def s = "$now: from - $workerId data = $data, $sum"
		return s
	}
	
}
