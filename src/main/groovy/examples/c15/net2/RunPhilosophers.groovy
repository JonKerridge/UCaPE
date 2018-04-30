package examples.c15.net2
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*
import examples.c12.canteen.*

def chefNodeIP = "127.0.0.1"
def canteenNodeIP = "127.0.0.2"
def philosopherNodeIP = "127.0.0.3"

def philosopherNodeAddr = new TCPIPNodeAddress(philosopherNodeIP,3002)
Node.getInstance().init (philosopherNodeAddr)
def gotOne = NetChannel.net2any() 
println "gotOne location = ${gotOne.getLocation()}"

def canteenAddress = new TCPIPNodeAddress(canteenNodeIP,3000)
def getOne = NetChannel.any2net(canteenAddress,51)
println "getOne location = ${getOne.getLocation()}"

getOne.write(0)
def philList = ( 0 .. 4 ).collect{
  i -> return new Philosopher(philosopherId:i, service:getOne, deliver:gotOne)
  }
new PAR ( philList ).run()  
   