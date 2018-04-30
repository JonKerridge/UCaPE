package examples.c07
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*
import groovyJCSP.*

def S02S1request = Channel.one2one()
def S12S0send = Channel.one2one()
def S12S0request = Channel.one2one()
def S02S1send = Channel.one2one()
def C02S0request = Channel.one2one()
def S02C0send = Channel.one2one()
def C12S1request = Channel.one2one()
def S12C1send = Channel.one2one()

def server0Map = [1:10,2:20,3:30,4:40,5:50,6:60,7:70,8:80,9:90,10:100]
def server1Map = [11:110,12:120,13:130,14:140,15:150,
	              16:160,17:170,18:180,19:190,20:200] 
       
def client0List = [1,12,3,14,15,16,7,18,9,10]
def client1List = [11,12,13,14,15,6,17,8,19,20]

def client0 = new Client ( requestChannel: C02S0request.out(),
                            receiveChannel: S02C0send.in(),
                            selectList: client0List,
                            clientNumber: 0)

def client1 = new Client ( requestChannel: C12S1request.out(),
                            receiveChannel: S12C1send.in(),
                            selectList: client1List,
                            clientNumber: 1)

def server0 = new Server ( clientRequest: C02S0request.in(),
                            clientSend: S02C0send.out(),
                            thisServerRequest: S02S1request.out(),
                            thisServerReceive: S12S0send.in(),
                            otherServerRequest: S12S0request.in(),
                            otherServerSend: S02S1send.out(),
                            dataMap: server0Map)

def server1 = new Server ( clientRequest: C12S1request.in(),
                            clientSend: S12C1send.out(),
                            thisServerRequest: S12S0request.out(),
                            thisServerReceive: S02S1send.in(),
                            otherServerRequest: S02S1request.in(),
                            otherServerSend: S12S0send.out(),
                            dataMap: server1Map)

def network = [client0, client1, server0, server1]
new PAR (network).run()

