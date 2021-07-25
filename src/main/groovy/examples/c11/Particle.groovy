package examples.c11
 

// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com


import jcsp.lang.*

class Particle implements CSProcess {
	
  ChannelOutput sendPosition
  ChannelInput getPosition
  int x = 100         // initial x location
  int y = 100         // initial y location
  def long delay = 200    // delay between movements
  int id
  int temperature = 25 // in range 10 to 50
  
  void run() {
    def timer = new CSTimer()
    def rng = new Random()
    def p = new Position ( id: id, px: x, py: y, temperature: temperature )
    while (true) {
      p.lx = p.px + rng.nextInt(p.temperature) - ( p.temperature / 2 )
      p.ly = p.py + rng.nextInt(p.temperature) - ( p.temperature / 2 )
      sendPosition.write ( p )
      p = ( (Position)getPosition.read() ).copy()   // p now has updated position
      timer.sleep ( delay )
    }
  }
}

