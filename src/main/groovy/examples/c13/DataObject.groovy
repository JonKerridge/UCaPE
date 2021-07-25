package examples.c13

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import groovy_jcsp.*


class DataObject implements Serializable, JCSPCopy {
	
  int pid
  int location
  int value
  
  def DataObject copy () {
    def dObj = new DataObject ( pid: this.pid,
                                location: this.location,
                                value: this.value 
                             )
    return dObj
  }
  
  def String toString() {
    def s = "[DataObject: pid:${pid}, location:${location}, value:${value}]"
    return s
  }
                              
}
