package examples.c05

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class Queue implements CSProcess {  
	
  def ChannelInput  put
  def ChannelInput  get
  def ChannelOutput receive
  def int elements = 5   
  
  void run() {
    def qAlt = new ALT ( [ put, get ] )
    def preCon = new boolean[2]
    def PUT = 0
    def GET = 1
    preCon[PUT] = true    
    preCon[GET] = false  
    def data = []
    def counter = 0       
    def front = 0       
    def rear = 0     
    def running = true
	
    while (running) {
      def index = qAlt.priSelect(preCon)
      switch (index) {
        case PUT:
          data[front] = put.read()
          front = (front + 1) % elements
          counter = counter + 1
          break
        case GET:
          get.read()      
          receive.write( data[rear])
          if (data[rear] == null) running = false          
          rear = (rear + 1) % elements
          counter = counter - 1
          break
      }
      preCon[PUT] = (counter < elements)
      preCon[GET] = (counter > 0 )
    }
    println "Q finished"
  }
}

          