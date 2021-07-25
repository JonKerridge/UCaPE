package examples.c07

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovy_jcsp.*


class Server implements CSProcess{
	  
  ChannelInput clientRequest
  ChannelOutput clientSend
  ChannelOutput thisServerRequest
  ChannelInput thisServerReceive
  ChannelInput otherServerRequest
  ChannelOutput otherServerSend
  def dataMap = [ : ]    
                
  void run () {
    def CLIENT = 0
    def OTHER_REQUEST = 1
    def THIS_RECEIVE = 2
    def serverAlt = new ALT ([clientRequest, 
		                      otherServerRequest, 
							  thisServerReceive])
    while (true) {
      def index = serverAlt.select()
	  
      switch (index) {		  
        case CLIENT :
          def key = clientRequest.read()
          if ( dataMap.containsKey(key) ) 
            clientSend.write(dataMap[key])          
          else 
            thisServerRequest.write(key)
          //end if 
          break
        case OTHER_REQUEST :
          def key = otherServerRequest.read()
          if ( dataMap.containsKey(key) ) 
            otherServerSend.write(dataMap[key])          
          else 
            otherServerSend.write(-1)
          //end if 
          break
        case THIS_RECEIVE :
          clientSend.write(thisServerReceive.read() )
          break
      } // end switch              
    } //end while   
  } //end run
}
