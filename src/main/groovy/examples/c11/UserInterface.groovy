package examples.c11
 
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.*
import jcsp.lang.*
import jcsp.awt.*
import java.awt.*

class UserInterface implements CSProcess {
	
  def ActiveCanvas particleCanvas
  def int canvasSize
  def ChannelInput tempValueConfig
  def ChannelInput pauseButtonConfig
  def ChannelOutput buttonEvent
  
  void run() {
    def root = new ActiveClosingFrame ("Brownian Motion Particle System")
    def mainFrame = root.getActiveFrame()
    def tempLabel = new Label ("Temperature")
    def tempValue = new ActiveLabel (tempValueConfig)
    tempValue.setAlignment( Label.CENTER)
    def upButton = new ActiveButton (null, buttonEvent, "Up")
    def downButton = new ActiveButton (null, buttonEvent, "Down")
    def pauseButton = new ActiveButton (pauseButtonConfig, buttonEvent, "START" )
    def tempContainer = new Container()
    tempContainer.setLayout ( new GridLayout ( 1, 5 ) )
    tempContainer.add ( pauseButton )
    tempContainer.add ( tempLabel )
    tempContainer.add ( upButton )
    tempContainer.add ( tempValue )
    tempContainer.add ( downButton )
    mainFrame.setLayout( new BorderLayout() )
    particleCanvas.setSize (canvasSize, canvasSize) 
    mainFrame.add (particleCanvas, BorderLayout.CENTER)
    mainFrame.add (tempContainer, BorderLayout.SOUTH)
    mainFrame.pack()
    mainFrame.setVisible ( true )
    def network = [ root, particleCanvas, tempValue, upButton, downButton, pauseButton ]
    new PAR (network).run()
  }
}

    
    