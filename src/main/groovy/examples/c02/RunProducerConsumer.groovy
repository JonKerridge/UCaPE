package examples.c02
 
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovy_jcsp.*

def connect = Channel.one2one()

def processList = [ new Producer ( outChannel: connect.out() ),
                    new Consumer ( inChannel: connect.in() )
                  ]
new PAR (processList).run()   
