package examples.c25

import jcsp.awt.*
import jcsp.lang.*
import jcsp.util.*
import groovy_jcsp.*

class Controller implements CSProcess {
	int maxPlayers = 5
	
	void run(){
		def dList = new DisplayList()
		def gameCanvas = new ActiveCanvas()	
		gameCanvas.setPaintable(dList)	
		def statusConfig = Channel.one2one()
		def IPlabelConfig = Channel.one2one()
		def pairsConfig = Channel.one2one()
		def playerNames = Channel.one2oneArray(maxPlayers)
		def pairsWon = Channel.one2oneArray(maxPlayers)
		def playerNamesIn = new ChannelInputList(playerNames)
		def playerNamesOut = new ChannelOutputList(playerNames)
		def pairsWonIn = new ChannelInputList(pairsWon)
		def pairsWonOut = new ChannelOutputList(pairsWon)
		
		def network = [ new ControllerManager ( dList: dList,
												statusConfig: statusConfig.out(),
												IPlabelConfig: IPlabelConfig.out(),
												pairsConfig: pairsConfig.out(),
												playerNames: playerNamesOut,
												pairsWon: pairsWonOut,
												maxPlayers: maxPlayers
											  ),
						new ControllerInterface( gameCanvas: gameCanvas,
												 statusConfig: statusConfig.in(),
												 IPlabelConfig: IPlabelConfig.in(),
												 pairsConfig: pairsConfig.in(),
												 playerNames: playerNamesIn,
												 pairsWon: pairsWonIn
											   )
				  ]
		new PAR (network).run()
	}

}
