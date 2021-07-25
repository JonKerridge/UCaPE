package examples.c11
 // copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import groovy_jcsp.*
import jcsp.lang.*
import jcsp.userIO.*
import jcsp.util.*
import jcsp.awt.*

class ParticleInterface implements CSProcess {
	
  ChannelInput inChannel
  ChannelOutput outChannel
  int canvasSize = 100
  int particles
  int centre
  int initialTemp
  
  void run() {
    def dList = new DisplayList()
    def particleCanvas = new ActiveCanvas()
    particleCanvas.setPaintable (dList)
    def tempConfig = Channel.one2one()
    def pauseConfig = Channel.one2one()
    def uiEvents = Channel.any2one( new OverWriteOldestBuffer(5) )
    def network = [ new ParticleManager ( fromParticles: inChannel, 
                                          toParticles: outChannel,
                                          toUI: dList,
                                          fromUIButtons: uiEvents.in(),
                                          toUIPause: pauseConfig.out(),
                                          toUILabel: tempConfig.out(),
                                          CANVASSIZE: canvasSize,
                                          PARTICLES: particles,
                                          CENTRE: centre,
                                          START_TEMP: initialTemp ),
                    new UserInterface   ( particleCanvas: particleCanvas, 
                                          canvasSize: canvasSize,
                                          tempValueConfig: tempConfig.in(),
                                          pauseButtonConfig: pauseConfig.in(),
                                          buttonEvent: uiEvents.out()  )
                  ]
    new PAR ( network ).run()
  }
}
   