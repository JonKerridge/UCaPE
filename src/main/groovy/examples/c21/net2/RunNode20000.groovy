package examples.c21.net2

import groovy_jcsp.*

def dataGenIP = "127.0.0.1"
def gathererIP = "127.0.0.2"

def pList = [ new Type2Process()]                
def vList = [ new Type2Process()]
                              
def processList = new NodeProcess ( nodeId: 2,
									                   nodeIPFinalPart: 4,
                                     toGathererIP: gathererIP,
                                     toDataGenIP: dataGenIP,
                                     processList: pList,
                                     vanillaList: vList
                                   )

new PAR ([ processList]).run()
