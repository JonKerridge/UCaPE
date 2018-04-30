package examples.c23.loaderObjects

import jcsp.lang.*






interface WorkerInterface extends CSProcess, Serializable{

	abstract connect(inChannels, outChannels)  
}
