package examples.c24.Distributed.loaderObjects

import jcsp.lang.*






interface WorkerInterface extends CSProcess, Serializable{

	abstract connect(inChannels, outChannels)  
}
