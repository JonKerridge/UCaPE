package examples.c07
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

def a = Channel.one2one()
def b = Channel.one2one()

def pList = [ new BadP ( inChannel: a.in(), outChannel: b.out() ),
              new BadC ( inChannel: b.in(), outChannel: a.out() )
            ]

new PAR (pList).run()