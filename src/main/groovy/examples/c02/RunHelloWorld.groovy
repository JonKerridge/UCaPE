package examples.c02

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

def connect = Channel.one2one()

def processList = [ 
                    new ProduceHW ( outChannel: connect.out() ),
                    new ConsumeHello ( inChannel: connect.in() )
                  ]
new PAR (processList).run()  
                 