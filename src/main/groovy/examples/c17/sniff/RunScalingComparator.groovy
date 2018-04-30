package examples.c17.sniff
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

def Copy2Sniff = Channel.one2one()
def Out2Comp = Channel.one2one()

def network = [ new SnifferComparator ( fromCopy: Copy2Sniff.in(),
                                         fromScaler: Out2Comp.in(),
                                         interval: 15000 ), 
									 
                new ScalingSystem ( toSniffer: Copy2Sniff.out(),
                                    toComparator: Out2Comp.out() ) 
             ]
new PAR (network).run()
