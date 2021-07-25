package examples.c18.net

import jcsp.lang.*
import groovy_jcsp.MobileAgent

class Agent implements MobileAgent {
  
  ChannelOutput toLocal
  ChannelInput fromLocal
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