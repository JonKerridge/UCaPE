package examples.c21.net2








class Type3 implements Serializable {

    def typeName = "Type3"
    def int typeInstance 
    def int instanceValue
    
  def processedNode
  
  def modify ( nodeId) {
	processedNode = nodeId
    typeInstance = typeInstance + (nodeId *10000)
  }
  
  def String toString(){
    return "Processing Node: $processedNode, Type: $typeName, " +
            "TypeInstanceValue: $typeInstance, Sequence: $instanceValue"
  }
}