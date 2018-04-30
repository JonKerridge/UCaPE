package examples.c14
     // copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import groovyJCSP.*
import jcsp.awt.*
import jcsp.util.*
import jcsp.userIO.*

def delay = Ask.Int("Target visible period (2000 to 3500)?  ", 2000, 3500)
 
def targets = 16
def targetOrigins = [ [10,  10],[120,  10],[230,  10],[340,  10],
                      [10, 120],[120, 120],[230, 120],[340, 120],
                      [10, 230],[120, 230],[230, 230],[340, 230],
                      [10, 340],[120, 340],[230, 340],[340, 340] ]
                      
def setUpBarrier = new Barrier(targets + 5)
def initBarrier = new Barrier()
def goBarrier= new Barrier(3)

def timeAndHitBarrier = AltingBarrier.create(targets+2)
def finalBarrier = AltingBarrier.create(2)

def buckets = Bucket.create(targets)

def mouseEvent = Channel.one2one ( new OverWriteOldestBuffer(20) )
def requestPoint = Channel.one2one()
def receivePoint = Channel.one2one()
def pointToTC = Channel.one2one( new OverWriteOldestBuffer(1) )

def targetsFlushed = Channel.one2one()
def flushNextBucket = Channel.one2one()

def targetsActivated = Channel.one2one()
def targetsActivatedToDC = Channel.one2one()
def getActiveTargets = Channel.one2one()

def hitsToGallery = Channel.one2one()
def possiblesToGallery = Channel.one2one()

def targetIdToManager = Channel.any2one()
def targetStateToDC = Channel.any2one()

def mousePointToTP = Channel.one2oneArray(targets)
def mousePoints = new ChannelOutputList ( mousePointToTP )

def imageList = new DisplayList()
def targetCanvas = new ActiveCanvas ()
targetCanvas.setPaintable ( imageList )
 
def targetList = ( 0 ..< targets ).collect { i ->
                  return new TargetProcess ( 
                      targetRunning: targetIdToManager.out(),
                      stateToDC: targetStateToDC.out(),
                      mousePoint: mousePointToTP[i].in(),
                      setUpBarrier: setUpBarrier,
                      initBarrier: initBarrier,
                      goBarrier: goBarrier,
                      timeAndHitBarrier: timeAndHitBarrier[i],
                      buckets: buckets,
                      targetId: i, 
                      x: targetOrigins[i][0],
                      y: targetOrigins[i][1],
                      delay: delay
                      )
                  }

def barrierManager = new BarrierManager ( 
                      timeAndHitBarrier: timeAndHitBarrier[targets],
                      finalBarrier: finalBarrier[0] ,
                      goBarrier: goBarrier,
                      setUpBarrier: setUpBarrier
                      )

def targetController = new TargetController (
                      getActiveTargets: getActiveTargets.out(),
                      activatedTargets: targetsActivated.in(),
                      receivePoint: pointToTC.in(),
                      sendPoint: mousePoints,
                      setUpBarrier: setUpBarrier,
                      goBarrier: goBarrier,
                      timeAndHitBarrier: timeAndHitBarrier[targets + 1]
                      )

def galleryProcess = new Gallery ( 
                      targetCanvas: targetCanvas,
                      hitsFromGallery: hitsToGallery.in(),
                      possiblesFromGallery: possiblesToGallery.in(),    
                      mouseEvent: mouseEvent.out() 
                      )

def flusher = new TargetFlusher (  
                      buckets: buckets,
                      targetsFlushed: targetsFlushed.out(),
                      flushNextBucket: flushNextBucket.in(),
                      initBarrier: initBarrier
                      )

def mouseBuffer = new MouseBufferPreCon (
                      mouseEvent: mouseEvent.in(),
                      getClick: requestPoint.in(),
                      sendPoint: receivePoint.out()
                      )

def mouseBufferPrompt = new MouseBufferPrompt (
                      returnPoint: pointToTC.out(),
                      getPoint: requestPoint.out(),
                      receivePoint: receivePoint.in(),
                      setUpBarrier: setUpBarrier
                      )

def targetManager = new TargetManager (
                      targetIdFromTarget: targetIdToManager.in(),
                      getActiveTargets: getActiveTargets.in(),
                      activatedTargets: targetsActivated.out(),
                      activatedTargetsToDC: targetsActivatedToDC.out(),
                      targetsFlushed: targetsFlushed.in(),
                      flushNextBucket: flushNextBucket.out(),
                      setUpBarrier: setUpBarrier
                      )

def displayControl = new DisplayController (
                      stateChange: targetStateToDC.in(),
                      activeTargets: targetsActivatedToDC.in(),
                      displayList: imageList,
                      hitsToGallery: hitsToGallery.out(),
                      possiblesToGallery: possiblesToGallery.out(),
                      setUpBarrier: setUpBarrier,
                      goBarrier: goBarrier,
                      finalBarrier: finalBarrier[1]     
                      )

def procList = targetList + 
               mouseBuffer +
               mouseBufferPrompt +
               targetManager + 
               galleryProcess + 
               displayControl +
               flusher + 
               targetController +
               barrierManager

new PAR ( procList ).run()  

