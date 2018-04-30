package examples.c25

import groovyJCSP.*
import jcsp.lang.*
import java.awt.event.*
import java.awt.event.MouseEvent

class MouseBuffer implements CSProcess {
	ChannelInput mouseEvent
	ChannelInput getPoint
	ChannelOutput sendPoint
	
	void run(){
		def alt = new ALT([getPoint, mouseEvent])
		def preCon = new boolean[2]
		def GET = 0
		preCon[GET] = false
		preCon[1] = true
		def point
		
		while (true){
			switch ( alt.select(preCon)) {
				case GET :
					getPoint.read()
					sendPoint.write(new MousePoint (point: point))
					preCon[GET] = false
					break
				case 1: // mouse event
					def mEvent = mouseEvent.read()
					if (mEvent.getID() == MouseEvent.MOUSE_PRESSED) {
						preCon[GET] = true
						def pointValue = mEvent.getPoint()
						point = [(int)pointValue.x, (int)pointValue.y]
					}
					break
			} // end of switch
		} // end while
	} // end run
}
