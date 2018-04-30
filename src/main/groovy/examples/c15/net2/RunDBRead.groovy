package examples.c15.net2
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*
import jcsp.userIO.*
import groovyJCSP.plugAndPlay.*
import examples.c13.Read

def dbIp = "127.0.0.1"
def readBase = 100
def readerBaseIP = "127.0.0."
def readerId = Ask.Int ("Reader process ID (0..4)? ", 0, 4)
def readerIndex = readBase + readerId
def readerIP = readerBaseIP + readerIndex
def readerAddress = new TCPIPNodeAddress(readerIP, 1000)
Node.getInstance().init(readerAddress)
    
println "Read Process $readerId, $readerIP is creating its Net channels "

//NetChannelInput 
def fromDB = NetChannel.numberedNet2One(75)  // the net2 channel from the database
println "fromDB location = ${fromDB.getLocation()}"

//NetChannelOutput 
def dbAddress =  new TCPIPNodeAddress(dbIp, 3000)
def toDB = NetChannel.one2net(dbAddress, readerIndex) // the net2 channel to the database
println "toDB location = ${toDB.getLocation()}"
toDB.write(0)
fromDB.read()

println "Read Process $readerId has created its Net channels "
def consoleChannel = Channel.one2one()
def pList = [ 
	new Read ( id:readerId, r2db: toDB, db2r: fromDB, toConsole: consoleChannel.out() ),
	new GConsole(toConsole:consoleChannel.in(), frameLabel: "Reader $readerId"  ) 
	]
new PAR (pList).run()
