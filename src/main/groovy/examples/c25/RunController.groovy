package examples.c25

import jcsp.awt.*
import jcsp.lang.*
import jcsp.util.*
import groovyJCSP.*

//enrol = Channel.createOne2One()
//fromPlayers = Channel.createOne2One()
//toPlayers = Channel.createOne2One(8)
//def toPlayersList = new ChannelOutputList(toPlayers)
/*
control =new Controller ( enrol: enrol.in(),
						  fromPlayers: fromPlayers.in(),
						  toPlayers: toPlayersList )
*/

control = new Controller()

new PAR ([control]).run()