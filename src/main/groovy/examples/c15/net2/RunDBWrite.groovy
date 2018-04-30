package examples.c15.net2
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*
import jcsp.userIO.*
import groovyJCSP.plugAndPlay.*
import examples.c13.Write

def dbIp = "127.0.0.1"
def writeBase = 200
def writerBaseIP = "127.0.0."
def writerId = Ask.Int ("Writer process ID (0..4)? ", 0, 4)
def writerIndex = writeBase + writerId
def writerIP = writerBaseIP + writerIndex
def writerAddress = new TCPIPNodeAddress(writerIP, 2000)
Node.getInstance().init(writerAddress)
    
println "Write Process $writerId, $writerIP is creating its Net channels "

//NetChannelInput 
def fromDB = NetChannel.numberedNet2One(150)  // the net2 channel from the database
println "fromDB location = ${fromDB.getLocation()}"

//NetChannelOutput
def dbAddress =  new TCPIPNodeAddress(dbIp, 3000)
def toDB = NetChannel.one2net(dbAddress, writerIndex) // the net2 channel to the database
println "toDB location = ${toDB.getLocation()}"
toDB.write(0)
fromDB.read()

println "Write Process $writerId has created its Net channels "
def consoleChannel = Channel.one2one()

def pList = [ 
	new Write ( id:writerId, w2db:toDB, db2w:fromDB, toConsole: consoleChannel.out()  ),
	new GConsole(toConsole:consoleChannel.in(), frameLabel: "Writer $writerId"  )  
	]
new PAR (pList).run()
