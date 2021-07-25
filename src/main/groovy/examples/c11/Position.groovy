package examples.c11
 
// copyright 2012-21 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com



import groovy_jcsp.*

class Position implements JCSPCopy {
	
  int id            // particle number
  int lx            // current x location of particle
  int ly            // current y location of particle
  int px            // previous x location of particle
  int py            // previous y location of particle
  int temperature   // current working temperature
  
  def copy() {
    def p = new Position ( id: this.id,  
                           lx: this.lx, ly: this.ly, 
                           px: this.px, py: this.py,
                           temperature : this.temperature )
    return p
  }

  def String toString () {
    def s = "[Position-> " + id + ", " + lx + ", " + ly 
    s = s + ", " + px + ", " + py
    s = s + ", " + temperature + " ]"
    return s
  }
}
