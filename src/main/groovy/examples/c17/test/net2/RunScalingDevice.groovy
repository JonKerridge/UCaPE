package examples.c17.test.net2

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.tcpip.*

def testPartIP = "127.0.0.1"  
def deviceIP = "127.0.0.2"
def testPartAddr = new TCPIPNodeAddress(testPartIP, 3000)
def deviceAddr = new TCPIPNodeAddress(deviceIP, 3000)
Node.getInstance().init(deviceAddr)

def ordinaryInput = NetChannel.net2one()
println "ordinaryInput location = ${ordinaryInput.getLocation()}"
ordinaryInput.read()
def scaledOutput = NetChannel.one2net(testPartAddr, 51)
println "scaledOutput location = ${scaledOutput.getLocation()}"

new PAR(new ScalingDevice (inChannel: ordinaryInput, outChannel: scaledOutput) ).run()
 