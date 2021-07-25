package examples.c16.net
 
import jcsp.lang.*
import groovy_jcsp.*
import jcsp.net.*
import jcsp.net.cns.*

class PrintJob  implements Serializable{

  int userId
  def NetChannelLocation useLocation
}