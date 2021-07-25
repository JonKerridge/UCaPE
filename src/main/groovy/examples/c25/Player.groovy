package examples.c25

import jcsp.awt.*
import jcsp.lang.*
import jcsp.util.*
import groovy_jcsp.*

class Player implements CSProcess {
//	ChannelOutput enrol
//	ChannelInput fromController
//	ChannelOutput toController
	int maxPlayers = 5
	
	void run(){
		def dList = new DisplayList()
		def gameCanvas = new ActiveCanvas()
		gameCanvas.setPaintable(dList)
		def IPlabelConfig = Channel.one2one()
		def IPenterField = Channel.one2one (new OverWriteOldestBuffer (5))
		def IPfieldConfig = Channel.one2one()
		def playerNames = Channel.one2oneArray(maxPlayers)
		def pairsWon = Channel.one2oneArray(maxPlayers)
		def playerNamesIn = new ChannelInputList(playerNames)
		def playerNamesOut = new ChannelOutputList(playerNames)
		def pairsWonIn = new ChannelInputList(pairsWon)
		def pairsWonOut = new ChannelOutputList(pairsWon)
		def nextButtonChannel = Channel.one2one (new OverWriteOldestBuffer (5))
		def withdrawButtonChannel = Channel.one2one (new OverWriteOldestBuffer (5))
		def mouseEvent = Channel.one2one (new OverWriteOldestBuffer (5))
		def nextPairConfig = Channel.one2one()
		def getValidPoint = Channel.one2one()
		def validPoint = Channel.one2one()
		def receivePoint = Channel.one2one()
		def getPoint = Channel.one2one()

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
