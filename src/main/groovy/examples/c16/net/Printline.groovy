package examples.c16.net
 
import jcsp.lang.*
import groovy_jcsp.*
import jcsp.net.*
import jcsp.net.cns.*
import jcsp.net.tcpip.*
import jcsp.userIO.*


class Printline implements Serializable {
  
  def int printKey
  def String line
  
}
