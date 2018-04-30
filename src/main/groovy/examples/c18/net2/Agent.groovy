package examples.c18.net2

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.MobileAgent


class Agent implements MobileAgent {  
	
  def ChannelOutput toLocal
  def ChannelInput fromLocal
  
  def results = [ ]    
                
  def connect ( c ) {
    this.toLocal = c[0]
    this.fromLocal = c[1]
  }  
  def disconnect () {
    toLocal = null
    fromLocal = null
  }  
  void run() {
    toLocal.write (results)
    results = fromLocal.read()
  }
}
