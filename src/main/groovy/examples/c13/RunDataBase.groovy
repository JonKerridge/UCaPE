package examples.c13

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*
import jcsp.userIO.*

def nReaders = Ask.Int ( "Number of Readers ? ", 1, 5)
def nWriters = Ask.Int ( "Number of Writers ? ", 1, 5)
def connections = nReaders + nWriters

def toDatabase = Channel.one2oneArray(connections)
def fromDatabase = Channel.one2oneArray(connections)
def consoleData = Channel.one2oneArray(connections)

def toDB = new ChannelInputList(toDatabase)
def fromDB = new ChannelOutputList(fromDatabase)

def readers = ( 0 ..< nReaders).collect { r ->
                       return new Read   (id: r,
                    		              r2db: toDatabase[r].out(),
                    		              db2r: fromDatabase[r].in(),
		               					  toConsole: consoleData[r].out())
                       }

def writers = ( 0 ..<nWriters).collect { w -> 
                       int wNo = w + nReaders
                       return new Write  ( id: w,
                    		               w2db: toDatabase[wNo].out(),
                    		               db2w: fromDatabase[wNo].in(),
                    		               toConsole: consoleData[wNo].out())
                       }

def database = new DataBase ( inChannels:  toDB,
		                      outChannels: fromDB,
		                      readers: nReaders,
		                      writers: nWriters)

def consoles = ( 0 ..< connections).collect { c ->
                        def frameString = c < nReaders ? 
                        		"Reader " + c : 
                        		"Writer " + (c - nReaders)
	                    return new GConsole (toConsole: consoleData[c].in(),
	                    		             frameLabel: frameString )
                        }
def procList = readers + writers + database + consoles

new PAR(procList).run()
