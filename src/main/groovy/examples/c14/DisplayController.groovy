package examples.c14

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import groovyJCSP.*
import jcsp.awt.*
import java.awt.*

class DisplayController implements CSProcess {
  def ChannelInput stateChange
  def ChannelInput activeTargets
  
  def DisplayList displayList
  def ChannelOutput hitsToGallery
  def ChannelOutput possiblesToGallery
  
  def Barrier setUpBarrier
  def Barrier goBarrier
  def AltingBarrier finalBarrier  
  
  void run() {
    
    def GraphicsCommand [] targetGraphics = new GraphicsCommand [ 34 ]                                                                
    targetGraphics[0] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[1] = new GraphicsCommand.FillRect (0, 0, 450, 450)
    targetGraphics[2] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[3] = new GraphicsCommand.FillRect (10, 10, 100, 100)
    targetGraphics[4] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[5] = new GraphicsCommand.FillRect (120, 10, 100, 100)
    targetGraphics[6] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[7] = new GraphicsCommand.FillRect (230, 10, 100, 100)
    targetGraphics[8] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[9] = new GraphicsCommand.FillRect (340, 10, 100, 100)
    targetGraphics[10] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[11] = new GraphicsCommand.FillRect (10, 120, 100, 100)
    targetGraphics[12] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[13] = new GraphicsCommand.FillRect (120, 120, 100, 100)
    targetGraphics[14] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[15] = new GraphicsCommand.FillRect (230, 120, 100, 100)
    targetGraphics[16] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[17] = new GraphicsCommand.FillRect (340, 120, 100, 100)
    targetGraphics[18] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[19] = new GraphicsCommand.FillRect (10, 230, 100, 100)
    targetGraphics[20] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[21] = new GraphicsCommand.FillRect (120, 230, 100, 100)
    targetGraphics[22] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[23] = new GraphicsCommand.FillRect (230, 230, 100, 100)
    targetGraphics[24] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[25] = new GraphicsCommand.FillRect (340, 230, 100, 100)
    targetGraphics[26] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[27] = new GraphicsCommand.FillRect (10, 340, 100, 100)
    targetGraphics[28] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[29] = new GraphicsCommand.FillRect (120, 340, 100, 100)
    targetGraphics[30] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[31] = new GraphicsCommand.FillRect (230, 340, 100, 100)
    targetGraphics[32] = new GraphicsCommand.SetColor (Color.BLACK)
    targetGraphics[33] = new GraphicsCommand.FillRect (340, 340, 100, 100)
    
    def targetColour = [  
        [new GraphicsCommand.SetColor (Color.RED), 2],
        [new GraphicsCommand.SetColor (Color.GREEN), 4],
        [new GraphicsCommand.SetColor (Color.YELLOW), 6],
        [new GraphicsCommand.SetColor (Color.BLUE), 8],
        [new GraphicsCommand.SetColor (Color.ORANGE), 10],
        [new GraphicsCommand.SetColor (Color.MAGENTA), 12],
        [new GraphicsCommand.SetColor (Color.CYAN), 14],
        [new GraphicsCommand.SetColor (Color.PINK), 16],
        [new GraphicsCommand.SetColor (Color.BLUE), 18],
        [new GraphicsCommand.SetColor (Color.YELLOW), 20],
        [new GraphicsCommand.SetColor (Color.GREEN), 22],
        [new GraphicsCommand.SetColor (Color.RED), 24],
        [new GraphicsCommand.SetColor (Color.PINK), 26],
        [new GraphicsCommand.SetColor (Color.CYAN), 28],
        [new GraphicsCommand.SetColor (Color.MAGENTA), 30],
        [new GraphicsCommand.SetColor (Color.ORANGE), 32]
                       ]
    
    def CHANGE = 1
    def BARRIER = 0                      
    def TIMED_OUT = 0
    def HIT = 1                       
    def controllerAlt = new ALT ( [ finalBarrier, stateChange ] )

    def whiteSquare = new GraphicsCommand.SetColor(Color.WHITE)
    def blackSquare = new GraphicsCommand.SetColor(Color.BLACK)
    def graySquare = new GraphicsCommand.SetColor(Color.GRAY)

    def totalHits = 0
    def possibleTargets = 0    
    def timer = new CSTimer()

    displayList.set (targetGraphics)
    hitsToGallery.write (" " + totalHits)
    possiblesToGallery.write ( " " + possibleTargets )
    setUpBarrier.sync()
    
    while (true) {
      def active = true
      def runningTargets = activeTargets.read()  // a list of running target Ids
      possibleTargets = possibleTargets + runningTargets.size
      possiblesToGallery.write ( " " + possibleTargets )
      for ( t in runningTargets)
        displayList.change ( targetColour[t][0], targetColour[t][1])        
      goBarrier.sync()
      while (active) {
        switch (controllerAlt.priSelect()) {
          case CHANGE:
            def modification = stateChange.read()  // [ tId, state ]
            switch ( modification[1]) {
              case HIT:
                displayList.change ( whiteSquare, targetColour[modification[0]][1])
                totalHits = totalHits + 1
                hitsToGallery.write (" " + totalHits)
               break
              case TIMED_OUT:
                displayList.change ( graySquare, targetColour[modification[0]][1])
                break
            } // end switch modification
            break
          case BARRIER:
            active = false
            break
        } // end switch controllerAlt
      } // end of while active
      timer.sleep(1500)
      for ( tId in runningTargets ) 
          displayList.change ( blackSquare, targetColour[tId][1])
      timer.sleep ( 500)       
    } // end while true
  }
}
