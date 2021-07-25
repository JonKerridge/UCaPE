package examples.c08

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import groovy_jcsp.*
import jcsp.lang.*

class CSMux implements CSProcess {
	
  ChannelInputList inClientChannels
  ChannelOutputList outClientChannels
  ChannelInputList fromServers
  ChannelOutputList toServers
  def serverAllocation = [ ]	// list of lists of keys contained in each server
  
  void run() {
    def servers = toServers.size()
    def muxAlt = new ALT (inClientChannels)
    while (true) {
      def index = muxAlt.select()
      def key = inClientChannels[index].read()
      def server = -1
      for ( i in 0 ..< servers) {
        if (serverAllocation[i].contains(key)) {
          server = i
          break
        }        
      }
      toServers[server].write(key) 
      def value = fromServers[server].read()
      outClientChannels[index].write(value)        
    }
  }
}
