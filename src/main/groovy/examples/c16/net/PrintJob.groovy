package examples.c16.net
 
import jcsp.lang.*
import groovyJCSP.*
import jcsp.net.*
import jcsp.net.cns.*

class PrintJob  implements Serializable{

  def int userId
  def NetChannelLocation useLocation
}