package examples.c21.net2

import groovyJCSP.*

def dataGenIP = "127.0.0.1"
def gathererIP = "127.0.0.2"

def pList = [ ]
def vList = [ ]
              
def processList = new NodeProcess ( nodeId: 4,
									                   nodeIPFinalPart: 6,
                                     toGathererIP: gathererIP,
                                     toDataGenIP: dataGenIP,
                                     processList: pList,
                                     vanillaList: vList
                                   )

new PAR ([ processList]).run()
