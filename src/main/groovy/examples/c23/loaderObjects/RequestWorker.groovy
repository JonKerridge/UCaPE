package examples.c23.loaderObjects








class RequestWorker implements Serializable {
	
	def loadLocation //net2 channel input location used to read WorkerObject
	def nodeIP		 // Ip address of the worker node sending object to host
}
