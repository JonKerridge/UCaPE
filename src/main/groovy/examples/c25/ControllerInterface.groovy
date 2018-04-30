package examples.c25

import java.awt.*
import jcsp.awt.*
import jcsp.lang.*
import jcsp.util.*
import groovyJCSP.*

class ControllerInterface implements CSProcess{
	ActiveCanvas gameCanvas
	ChannelInput IPlabelConfig
	ChannelInput statusConfig
	ChannelInput pairsConfig
	ChannelInputList playerNames
	ChannelInputList pairsWon
	
	void run(){
		def root = new ActiveClosingFrame("PAIRS (Turn Over Game) - Main Controller")
		def mainFrame = root.getActiveFrame()
		mainFrame.setSize(900, 800)
		def statusLabel = new ActiveLabel(statusConfig)
		def pairsLabel = new ActiveLabel (pairsConfig)
		def IPLabel = new ActiveLabel (IPlabelConfig)
		pairsLabel.setAlignment(Label.LEFT)
		gameCanvas.setSize(560, 560)

		def buttonContainer = new Container()
		buttonContainer.setLayout(new GridLayout (1,5))
		buttonContainer.add(IPLabel)
		buttonContainer.add(new Label("STATUS"))
		buttonContainer.add(statusLabel)
		buttonContainer.add(new Label("Pairs Unclaimed"))
		buttonContainer.add(pairsLabel)
		
		def outcomeContainer = new Container()
		def maxPlayers = playerNames.size()
		def playerNameSpaces = []
		def playerWonSpaces = []
		for ( i in 0..<maxPlayers) {
			playerNameSpaces << new ActiveLabel (playerNames[i], "Player " + i)
			playerWonSpaces << new ActiveLabel (pairsWon[i], "  ")
		}
		outcomeContainer.setLayout(new GridLayout(1+maxPlayers,2))
		def nameLabel = new Label("Player Name")
		def wonLabel = new Label ("Pairs Won")
		
		outcomeContainer.add(nameLabel)
		outcomeContainer.add(wonLabel)
		
		for ( i in 0 ..< maxPlayers){
			outcomeContainer.add(playerNameSpaces[i])
			outcomeContainer.add(playerWonSpaces[i])
		}
		
		mainFrame.setLayout(new BorderLayout())
		mainFrame.add(gameCanvas, BorderLayout.CENTER)
		mainFrame.add(buttonContainer, BorderLayout.NORTH)
		mainFrame.add(outcomeContainer, BorderLayout.EAST)
		
		mainFrame.pack()
		mainFrame.setVisible(true)	
		def network = [root, gameCanvas, statusLabel, pairsLabel, IPLabel]
		network = network + playerNameSpaces + playerWonSpaces
		new PAR(network).run()
	}

}
