package examples.c16.net2
 // copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import jcsp.net2.*

class PrintUser implements CSProcess {
	
  def ChannelOutput printerRequest
  def ChannelOutput printerRelease
  def int userId  
  
  void run() {	  
    def printList = [ "line 1 for user " + userId, 
                      "line 2 for user " + userId,
                      "last line for user " + userId 
                    ]
    def useChannel = NetChannel.net2one()
    printerRequest.write(new PrintJob ( userId: userId,
                                         useLocation: useChannel.getLocation() ) )
    def printChannelLocation = useChannel.read()
    def useKey = useChannel.read()
    println "Print for user ${userId} accepted using Spooler $useKey"
    def printerChannel = NetChannel.one2net ( printChannelLocation)    
    printList.each { printerChannel.write (new Printline ( printKey: useKey, line: it) )}    
    printerRelease.write ( useKey )
    println "Print for user ${userId} completed"
  }
}
