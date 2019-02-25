package examples.c21.net2








class AvailableNodeList implements Serializable{

  def anl  = []// a list of available nodes

  AvailableNodeList clone() {
    List newanl
    newanl = []
    for ( i in 0 ..< anl.size()) newanl[i] = anl[i]
    return new AvailableNodeList(anl: newanl)
  }

}
