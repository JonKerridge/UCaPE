package examples.c08

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import groovyJCSP.*
import jcsp.lang.*

class CSMux implements CSProcess {
	
  def ChannelInputList inClientChannels
  def ChannelOutputList outClientChannels
  def ChannelInputList fromServers
  def ChannelOutputList toServers
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
