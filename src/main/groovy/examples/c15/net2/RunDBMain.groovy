package examples.c15.net2
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*
import jcsp.userIO.*

import examples.c13.DataBase

def dbIp = "127.0.0.1"
def dbAddress = new TCPIPNodeAddress(dbIp, 3000)
Node.getInstance().init(dbAddress)

int nReaders = Ask.Int ( "Number of Readers ? ", 1, 5)
int nWriters = Ask.Int ( "Number of Writers ? ", 1, 5)

def readerAddresses = []
def writerAddresses = []
def toDB = new ChannelInputList()
def fromDB = new ChannelOutputList()

println "Creating reader network channels"
def readBase = 100
def readerBaseIP = "127.0.0."

for ( readerId in 0 ..< nReaders  ) {
  def readerIndex = readBase + readerId
  def readerIP = readerBaseIP + readerIndex
  readerAddresses << new TCPIPNodeAddress(readerIP, 1000)
  toDB.append ( NetChannel.numberedNet2One(readerIndex) )
  println "Reader: $readerId, $readerIndex, $readerIP - " +
	  "toDB location = ${toDB[readerId].getLocation()}"
}
println "Creating writer network channels"
def writeBase = 200
def writerBaseIP = "127.0.0."

for ( writerId in 0 ..< nWriters  ) {
  def writerIndex = writeBase + writerId
  def writerIP = writerBaseIP + writerIndex
  writerAddresses << new TCPIPNodeAddress(writerIP, 2000)
  toDB.append ( NetChannel.numberedNet2One(writerIndex) )
  println "Writer: $writerId, $writerIndex, $writerIP - " +
	 " toDB location = ${toDB[writerId+nReaders].getLocation()}"
}

for ( r in 0 ..< nReaders){
  toDB[r].read()
  fromDB.append ( NetChannel.one2net ( readerAddresses[r], 75) )
  println "Reader $r fromDB location = ${fromDB[r].getLocation()}"
}

for ( w in 0..< nWriters){
  toDB[w + nReaders].read()
  fromDB.append ( NetChannel.one2net ( writerAddresses[w], 150) )
  println "Writer $w fromDB location = ${fromDB[w + nReaders].getLocation()}"
}

for ( c in 0 ..< (nReaders + nWriters)){
	fromDB[c].write(0)
}
println "DBM: Creating database process list"

def pList = [ new DataBase ( inChannels: toDB,
                             outChannels: fromDB,
                             readers: nReaders,
                             writers: nWriters ) ]
println "DBM: Running Database"

new PAR (pList).run()   
                           