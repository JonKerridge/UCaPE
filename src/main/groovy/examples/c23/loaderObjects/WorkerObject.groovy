package examples.c23.loaderObjects








class WorkerObject implements Serializable {
	def workerProcess		 // implements WorkerInterface
	def inConnections  = []	 // list of channel numbers
	def outConnections = []  // list of Net Output Connections [IP, cn] 
}
