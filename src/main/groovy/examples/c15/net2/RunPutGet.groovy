package examples.c15.net2

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

def comms = Channel.one2one()

def network = [new Put (outChannel: comms.out()),
			   new Get (inChannel: comms.in())]

new PAR(network).run()
