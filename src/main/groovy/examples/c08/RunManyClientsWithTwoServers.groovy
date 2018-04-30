package examples.c08
    
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.*
import jcsp.lang.*
import jcsp.userIO.Ask


import examples.c07.Client
def clients = Ask.Int ("Number of clients per server; 1 to 9 ? ", 1, 9)
def servers = 2

def C0ToM0 = Channel.one2oneArray (clients)
def M0ToC0 = Channel.one2oneArray (clients)
def C1ToM1 = Channel.one2oneArray (clients)
def M1ToC1 = Channel.one2oneArray (clients)
def M1ToS = Channel.one2oneArray (servers)
def M0ToS = Channel.one2oneArray (servers)
def S0ToM = Channel.one2oneArray (servers)
def S1ToM = Channel.one2oneArray (servers)

def clientsToM0 = new ChannelInputList (C0ToM0)
def clientsToM1 = new ChannelInputList (C1ToM1)
def M0ToClients = new ChannelOutputList(M0ToC0) 
def M1ToClients = new ChannelOutputList(M1ToC1) 
def Mux0ToServers = new ChannelOutputList(M0ToS)
def Mux1ToServers = new ChannelOutputList(M1ToS)
def Server0ToMuxes = new ChannelOutputList (S0ToM)
def Server1ToMuxes = new ChannelOutputList (S1ToM)

def Server0FromMuxes = new ChannelInputList()
Server0FromMuxes.append(M0ToS[0].in())
Server0FromMuxes.append(M1ToS[0].in())

def Server1FromMuxes = new ChannelInputList()
Server1FromMuxes.append(M0ToS[1].in())
Server1FromMuxes.append(M1ToS[1].in())

def Mux0FromServers = new ChannelInputList ()
Mux0FromServers.append(S0ToM[0].in())
Mux0FromServers.append(S1ToM[0].in())

def Mux1FromServers = new ChannelInputList ()
Mux1FromServers.append(S0ToM[1].in())
Mux1FromServers.append(S1ToM[1].in())

def server0Map = [1:10, 2:20, 3:30, 4:40, 5:50, 
				  6:60, 7:70, 8:80, 9:90, 10:100]
def server1Map = [11:110,12:120,13:130,14:140,15:150,
				  16:160,17:170,18:180,19:190,20:200]                  
def serverKeyLists = [ [1,2,3,4,5,6,7,8,9,10], 
					   [11,12,13,14,15,16,17,18,19,20] ]  
              
def client0List = [1,12,3,14,15,16,7,18,9,10]
def client1List = [11,12,13,14,15,6,17,8,19,20]

def network = [ ]                
def server0ClientList = (0 ..< clients).collect { i ->
				return new Client ( requestChannel: C0ToM0[i].out(),
                                    receiveChannel: M0ToC0[i].in(),
                                    clientNumber: i,
                                    selectList: client0List)
                                     }
def server1ClientList = (0 ..< clients).collect { i ->
                return new Client ( requestChannel: C1ToM1[i].out(),
                                    receiveChannel: M1ToC1[i].in(),
                                    clientNumber: i+10,
                                    selectList: client1List)
                                     }
network << new CSMux ( inClientChannels: clientsToM0,
                       outClientChannels: M0ToClients,
                       fromServers: Mux0FromServers,
                       toServers: Mux0ToServers,
                       serverAllocation: serverKeyLists)
network << new CSMux ( inClientChannels: clientsToM1,
                       outClientChannels: M1ToClients,
                       fromServers: Mux1FromServers,
                       toServers: Mux1ToServers,
                       serverAllocation: serverKeyLists)
network << new Server ( fromMux: Server0FromMuxes,
                        toMux: Server0ToMuxes,
                        dataMap: server0Map)                   
network << new Server ( fromMux: Server1FromMuxes,
                        toMux: Server1ToMuxes,
                        dataMap: server1Map)                   
new PAR(network + server0ClientList + server1ClientList).run()
             