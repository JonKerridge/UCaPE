package examples.c25

import java.awt.*
import jcsp.awt.*
import jcsp.lang.*
import jcsp.util.*
import groovyJCSP.*

class PlayerInterface implements CSProcess{
	ActiveCanvas gameCanvas
	ChannelInput IPlabel
	ChannelInput IPconfig
	ChannelOutput IPfield
	ChannelInputList playerNames
	ChannelInputList pairsWon
	ChannelOutput nextButton
	ChannelOutput withdrawButton
	ChannelOutput mouseEvent
	ChannelInput nextPairConfig
	
	void run(){
		def root = new ActiveClosingFrame("PAIRS (Turn Over Game) - Player Interface")
		def mainFrame = root.getActiveFrame()
		mainFrame.setSize(900, 850)
		def label = new ActiveLabel(IPlabel)
		label.setAlignment(Label.RIGHT)
		def text = new ActiveTextEnterField(IPconfig, IPfield, " ")
		def continueButton = new ActiveButton(nextPairConfig, nextButton, "                   ")
		def withdrawButton = new ActiveButton(null, withdrawButton, "Withdraw from Game")
		
		gameCanvas.setSize(560, 560)
		gameCanvas.addMouseEventChannel(mouseEvent)
		
		def labelContainer = new Container()
		labelContainer.setLayout(new GridLayout(1,2))
		labelContainer.add(label)
		labelContainer.add(text.getActiveTextField())
		
		def buttonContainer = new Container()
		buttonContainer.setLayout(new GridLayout(1,3))
		buttonContainer.add(withdrawButton)
		buttonContainer.add(new Label('           '))
		buttonContainer.add(continueButton)
		
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
		mainFrame.add(labelContainer, BorderLayout.NORTH)
		mainFrame.add(outcomeContainer, BorderLayout.EAST)
		mainFrame.add(buttonContainer, BorderLayout.SOUTH)
		
		mainFrame.pack()
		mainFrame.setVisible(true)	
		def network = [root, gameCanvas, label, text, withdrawButton, continueButton]	
		network = network + playerNameSpaces + playerWonSpaces
		new PAR(network).run()
	}

}
