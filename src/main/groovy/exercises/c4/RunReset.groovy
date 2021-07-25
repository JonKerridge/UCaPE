package exercises.c4

import jcsp.lang.*
import groovy_jcsp.*
import groovy_jcsp.plugAndPlay.*
import examples.c04.ResetUser
 
One2OneChannel RU2RN = Channel.one2one()

One2OneChannel RN2Conv = Channel.one2one()
One2OneChannel Conv2FD = Channel.one2one()
One2OneChannel FD2GC = Channel.one2one()

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
def  RU2GCClear= Channel.one2one()

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
