package examples.c18.net2

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import groovy_jcsp.*
import jcsp.net2.*

class BackAgent implements MobileAgent {
	
  ChannelOutput toLocal
  ChannelInput fromLocal
  def NetChannelLocation backChannel
  
  def results = [ ]    
                
  def connect ( c ) {
    this.toLocal = c[0]
    this.fromLocal = c[1]
  }
    
  def disconnect (){
    toLocal = null
    fromLocal = null
  }
  
  void run() {
    def toRoot = NetChannel.one2net (backChannel)
    toLocal.write (results)
    results = fromLocal.read()
    def last = results.size - 1
    toRoot.write(results[last])
  }  
}
