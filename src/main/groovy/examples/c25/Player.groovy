package examples.c25

import jcsp.awt.*
import jcsp.lang.*
import jcsp.util.*
import groovyJCSP.*

class Player implements CSProcess {
//	ChannelOutput enrol
//	ChannelInput fromController
//	ChannelOutput toController
	int maxPlayers = 5
	
	void run(){
		def dList = new DisplayList()
		def gameCanvas = new ActiveCanvas()
		gameCanvas.setPaintable(dList)
		def IPlabelConfig = Channel.createOne2One()
		def IPenterField = Channel.createOne2One (new OverWriteOldestBuffer (5))
		def IPfieldConfig = Channel.createOne2One()
		def playerNames = Channel.createOne2One(maxPlayers)
		def pairsWon = Channel.createOne2One(maxPlayers)
		def playerNamesIn = new ChannelInputList(playerNames)
		def playerNamesOut = new ChannelOutputList(playerNames)
		def pairsWonIn = new ChannelInputList(pairsWon)
		def pairsWonOut = new ChannelOutputList(pairsWon)
		def nextButtonChannel = Channel.createOne2One (new OverWriteOldestBuffer (5))
		def withdrawButtonChannel = Channel.createOne2One (new OverWriteOldestBuffer (5))
		def mouseEvent = Channel.createOne2One (new OverWriteOldestBuffer (5))
		def nextPairConfig = Channel.createOne2One()
		def getValidPoint = Channel.createOne2One()
		def validPoint = Channel.createOne2One()
		def receivePoint = Channel.createOne2One()
		def getPoint = Channel.createOne2One()

		def network = [ new PlayerManager (dList: dList,
										   IPlabel: IPlabelConfig.out(),
										   IPfield: IPenterField.in(),
										   IPconfig: IPfieldConfig.out(),
										   playerNames: playerNamesOut,
										   pairsWon: pairsWonOut,
										   nextButton: nextButtonChannel.in(),
										   withdrawButton: withdrawButtonChannel.in(),
										   getValidPoint: getValidPoint.out(),
										   validPoint: validPoint.in(),
										   nextPairConfig: nextPairConfig.out()
										  ),
						new PlayerInterface (gameCanvas: gameCanvas,
										     IPlabel: IPlabelConfig.in(),
											 IPfield: IPenterField.out(),
											 IPconfig: IPfieldConfig.in(),
											 playerNames: playerNamesIn,
											 pairsWon: pairsWonIn,
										     nextButton: nextButtonChannel.out(),
										     withdrawButton: withdrawButtonChannel.out(),
											 mouseEvent: mouseEvent.out(),
											 nextPairConfig: nextPairConfig.in()
											 ),
						new Matcher ( getValidPoint: getValidPoint.in(),
									  validPoint: validPoint.out(),
									  receivePoint: receivePoint.in(),
									  getPoint: getPoint.out()),
						new MouseBuffer ( mouseEvent: mouseEvent.in(),
										  getPoint: getPoint.in(),
										  sendPoint: receivePoint.out())
					  ]
		new PAR (network).run()
	}
}
