package examples.c25

import jcsp.awt.*
import jcsp.lang.*
import jcsp.util.*
import groovy_jcsp.*

//enrol = Channel.one2one()
//fromPlayers = Channel.one2one()
//toPlayers = Channel.one2oneArray(8)
//def toPlayersList = new ChannelOutputList(toPlayers)
/*
control =new Controller ( enrol: enrol.in(),
						  fromPlayers: fromPlayers.in(),
						  toPlayers: toPlayersList )
*/

control = new Controller()

new PAR ([control]).run()