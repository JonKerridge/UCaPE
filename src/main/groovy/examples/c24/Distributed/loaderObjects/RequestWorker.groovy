package examples.c24.Distributed.loaderObjects








class RequestWorker implements Serializable {
	
	def loadLocation //net2 channel input location used to read WorkerObject
	def nodeIP		 // Ip address of the worker node sending object to host
}
