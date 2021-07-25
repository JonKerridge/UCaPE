package examples.c15.net2

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

def comms = Channel.one2one()

def network = [new Put (outChannel: comms.out()),
			   new Get (inChannel: comms.in())]

new PAR(network).run()
