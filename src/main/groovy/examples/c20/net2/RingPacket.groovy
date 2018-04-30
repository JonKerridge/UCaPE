package examples.c20.net2

import groovyJCSP.*





class RingPacket implements Serializable, JCSPCopy {
	
  def int source
  def int destination
  def int value
  def boolean full
  
  def copy () {
    def p = new RingPacket ( source: this.source,
                              destination: this.destination,
                              value: this.value,
                              full: this.full)
    return p
  }

  def String toString () {
    def s = "Packet [ s: ${source}, d: ${destination}, v: ${value}, f: ${full} ] "
    return s
  }
}
