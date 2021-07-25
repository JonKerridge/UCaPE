package examples.c16.net2
 
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.net2.*

class PrintJob  implements Serializable{
	
  int userId
  def NetChannelLocation useLocation
}
