package examples.c09
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class EventData implements Serializable, JCSPCopy {  
	
  def int source = 0
  def int data = 0
  def int missed = -1 
   
  def copy() {
    def e = new EventData ( source: this.source, 
                            data: this.data, 
                            missed: this.missed )
    return e
  }  
  
  def String toString() {
    def s = "EventData -> [source: "
    s = s + source + ", data: "
    s = s + data + ", missed: " 
    s = s + missed + "]"
    return s
  }   
    
}


