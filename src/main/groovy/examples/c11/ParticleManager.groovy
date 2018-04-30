package examples.c11
 // copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovyJCSP.*
import jcsp.lang.*
import jcsp.userIO.*
import jcsp.awt.*
import java.awt.*

class ParticleManager implements CSProcess {
	
  def ChannelInput fromParticles
  def ChannelOutput toParticles
  def DisplayList toUI
  def int CANVASSIZE
  def int PARTICLES
  def int CENTRE
  def int START_TEMP
  def ChannelInput fromUIButtons
  def ChannelOutput toUILabel
  def ChannelOutput toUIPause  
  
  void run() {
    def colourList = [ Color.BLUE, Color.GREEN, 
                       Color.RED, Color.MAGENTA, 
                       Color.CYAN, Color.YELLOW]
    
    def temperature = START_TEMP                      

    GraphicsCommand [] particleGraphics = new GraphicsCommand [ 1 + (PARTICLES * 2) ]
    
    particleGraphics[0] = new GraphicsCommand.ClearRect ( 0, 0, CANVASSIZE, CANVASSIZE )

    GraphicsCommand [] initialGraphic = new GraphicsCommand [ 2 ]

    initialGraphic[0] = new GraphicsCommand.SetColor (Color.BLACK)
    initialGraphic[1] = new GraphicsCommand.FillOval (CENTRE, CENTRE, 10, 10)
    
    for ( i in 0 ..< PARTICLES ) {
      def p = (i * 2) + 1
      for ( j in 0 ..< 2) {
        particleGraphics [p+j] = initialGraphic[j]
      }
    }
    
    toUI.set (particleGraphics)  
    GraphicsCommand [] positionGraphic =  new GraphicsCommand [ 2 ]
    positionGraphic = 
      [ new GraphicsCommand.SetColor (Color.WHITE),
        new GraphicsCommand.FillOval (CENTRE, CENTRE, 10, 10)
      ]
      
    def pmAlt = new ALT ( [fromUIButtons, fromParticles] )
    
    def initTemp = " " + temperature + " "
    toUILabel.write ( initTemp ) 
    
    def direction = fromUIButtons.read()
    while ( direction != "START" ) {
      direction = fromUIButtons.read()
    }
    toUIPause.write("PAUSE")
                   
    while (true) {
      def index = pmAlt.priSelect()
      if ( index == 0 ) {        // dealing with a button event
        direction = fromUIButtons.read()
        if (direction == "PAUSE" ) {
          toUIPause.write("RESTART")
          direction = fromUIButtons.read()
          while ( direction != "RESTART" ) {
            direction = fromUIButtons.read()
          }
          toUIPause.write("PAUSE")
        }
        else {
          if (( direction == "Up" ) && ( temperature < 50 )) {
              temperature = temperature + 5
              def s = "+" + temperature + "+"
              toUILabel.write ( s )
          }
          else { 
            if ( (direction == "Down" ) && ( temperature > 10 ) ) {
              temperature = temperature - 5
              def s = "-" + temperature + "-"
              toUILabel.write ( s )
            }
            else {
            }
          }
        }
      }
      else {    // index is 1 particle movement
        def p = (Position) fromParticles.read()
        // make sure particle stays within bounds of canvas by bouncing off the boundary of the canvas
        if ( p.lx > CANVASSIZE ) { p.lx = (2 * CANVASSIZE) - p.lx }
        if ( p.ly > CANVASSIZE ) { p.ly = (2 * CANVASSIZE) - p.ly }
        if ( p.lx < 0 ) { p.lx = 0 - p.lx }
        if ( p.ly < 0 ) { p.ly = 0 - p.ly }
        // now change positionGraphic
        positionGraphic [0] = new GraphicsCommand.SetColor ( colourList.getAt(p.id%6 ) )
        positionGraphic [1] = new GraphicsCommand.FillOval (p.lx, p.ly, 10, 10)
        // now modify the DisplayList toUI
        toUI.change ( positionGraphic, 1 + ( p.id * 2) )
        // modify px, py
        p.px = p.lx
        p.py = p.ly
        p.temperature = temperature
        toParticles.write(p)
      } // index test
    } // while
  } // run
}
                   