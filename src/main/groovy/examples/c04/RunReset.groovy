package examples.c04

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import jcsp.lang.*
import groovyJCSP.*
import groovyJCSP.plugAndPlay.*

def RU2RN = Channel.one2one()

def RN2Conv = Channel.one2one()
def Conv2FD = Channel.one2one()
def FD2GC = Channel.one2one()

def RNprocList = [ new ResetNumbers ( resetChannel: RU2RN.in(), 
            						  initialValue: 1000,
            						  outChannel: RN2Conv.out() ),
            	   new GObjectToConsoleString  ( inChannel: RN2Conv.in(), 
            								     outChannel:Conv2FD.out() ),
            	   new GFixedDelay ( delay: 200,
            			             inChannel: Conv2FD.in(), 
            			             outChannel: FD2GC.out() ),
            	   new GConsole ( toConsole: FD2GC.in(),
            					  frameLabel: "Reset Numbers Console"  )
               ]	

def RU2GC = Channel.one2one()
def GC2Conv = Channel.one2one()
def Conv2RU = Channel.one2one()
def RU2GCClear= Channel.one2one()

def RUprocList = [ new ResetUser ( resetValue: RU2RN.out(),
					               toConsole: RU2GC.out(),
								   fromConverter: Conv2RU.in(),
								   toClearOutput: RU2GCClear.out()),
			       new GConsoleStringToInteger ( inChannel: GC2Conv.in(),  
	                		                     outChannel: Conv2RU.out()),
                   new GConsole ( toConsole: RU2GC.in(),
                        		  fromConsole: GC2Conv.out(),
                        		  clearInputArea: RU2GCClear.in(),
                        		  frameLabel: "Reset Value Generator" )
                ]
new PAR (RNprocList + RUprocList).run()
