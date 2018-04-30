package examples.c22.emitter;

import jcsp.lang.*

interface CopyOfManipulateInterface extends Serializable {
	
	abstract manipulate(id)
	abstract display(now)
}
