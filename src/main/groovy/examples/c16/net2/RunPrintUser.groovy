package examples.c16.net2
 // copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*
import jcsp.userIO.*

def printSpoolerIP = "127.0.0.1"
def printUserIPmask = "127.0.0."
def user = Ask.Int ("User Number (2 to 255) ? ", 2, 255)
def printUserIP = printUserIPmask + user

def printUserAddr = new TCPIPNodeAddress(printUserIP, 3000)
Node.getInstance().init(printUserAddr)
def printSpoolerAddr = new TCPIPNodeAddress(printSpoolerIP, 2000)

def pRequest = NetChannel.any2net  (printSpoolerAddr, 50)
def pRelease = NetChannel.any2net  (printSpoolerAddr, 51)

println "pRequest location = ${pRequest.getLocation()}"
println "pRelease location = ${pRelease.getLocation()}"

new PAR ( [ new PrintUser ( printerRequest: pRequest, 
                            printerRelease: pRelease, 
                            userId : user
                          )
          ] ).run()

