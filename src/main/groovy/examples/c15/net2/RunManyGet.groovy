package examples.c15.net2
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*
import jcsp.userIO.*

def numberOfGets = Ask.Int("How many get processes (2..9)?", 2, 9)

def manyGetNodeIP = "127.0.0.2"
def manyGetAddr = new TCPIPNodeAddress(manyGetNodeIP, 3000)
Node.getInstance().init (manyGetAddr)

def comms = NetChannel.net2any()
def pList = (0 ..< numberOfGets).collect{
	i -> new Get ( inChannel: comms, id: i ) 
	}

new PAR ( pList ).run()
