package examples.c14

// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com
import jcsp.lang.*
import groovyJCSP.*
import java.awt.Point
import java.awt.event.*

class MouseBufferPreCon implements CSProcess{
	
  def ChannelInput mouseEvent
  def ChannelInput getClick
  def ChannelOutput sendPoint

  void run() {
    def mouseBufferAlt = new ALT ( [ getClick, mouseEvent ] )
    def preCon = new boolean [2]
    def EVENT = 1
    def GET = 0
    preCon[EVENT]= true
    preCon[GET] = false
    def point
    while (true) {
      switch (mouseBufferAlt.select(preCon)) {
        case GET:
          getClick.read()
          sendPoint.write(point)
          preCon[GET] = false
          break
        case EVENT:
          def mEvent = mouseEvent.read()
          if ( mEvent.getID() == MouseEvent.MOUSE_PRESSED) {
            preCon[GET] = true
            point = mEvent.getPoint()
          }
          break
      }
    }
  }
}
