package examples.c20.net
 
import jcsp.lang.*
import groovy_jcsp.*

class RingPacket implements Serializable, JCSPCopy {
  int source
  int destination
  int value
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
