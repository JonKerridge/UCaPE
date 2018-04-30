package examples.c12.canteen

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

class QueuingCanteen implements CSProcess {
    
  def ChannelInput service    
  def ChannelOutput deliver   
  def ChannelInput supply     
  def ChannelOutput toConsole
    
  void run () {

    def canteenAlt = new ALT ([supply, service])
    def boolean [] precondition = [true, false ]
    
    def SUPPLY = 0
    def SERVICE = 1

    def tim = new CSTimer()
    def chickens = 0
      
    toConsole.write ("Canteen : starting ... \n")
	
    while (true) {
      precondition[SERVICE] = (chickens > 0)
      if (chickens == 0 ){
        toConsole.write ("Waiting for chickens ...\n")        
      }
      switch (canteenAlt.fairSelect (precondition)) {
        case SUPPLY: 
          def value = supply.read()        
          toConsole.write ("Chickens on the way ...\n")
          tim.after (tim.read() + 3000)  
          chickens = chickens + value
          toConsole.write ("$chickens chickens now available ...\n")
          supply.read()                 
        break
        case SERVICE:
          def id = service.read()           
          chickens = chickens - 1
          toConsole.write ("chicken ready for Philosoper $id ... $chickens chickens left \n")
          deliver.write(1)    
        break
      }
    }
  }
}
