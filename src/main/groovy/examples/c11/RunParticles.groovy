package examples.c11
  
// copyright 2012-18 Jon Kerridge
// Using Concurrency and Parallelism Effectively parts i & ii, 2014, bookboon.com

import groovyJCSP.*
import jcsp.lang.*
import jcsp.userIO.*

def  connect = Channel.any2one()
def  update = Channel.any2one()

def CSIZE = Ask.Int ("Size of Canvas (200, 600)?: ", 200, 600)
def CENTRE = CSIZE / 2
def PARTICLES = Ask.Int ("Number of Particles (10, 200)?: ", 10, 200)
def INIT_TEMP = 20

def network = []
for ( i in 0..< PARTICLES ) {
  network << new Particle ( id: i, 
                            sendPosition: connect.out(),
                            getPosition: update.in(), 
                            x: CENTRE, 
                            y: CENTRE, 
                            temperature: INIT_TEMP )
}
 
network << ( new ParticleInterface ( inChannel: connect.in(), 
                                     outChannel: update.out(), 
                                     canvasSize: CSIZE,
                                     particles: PARTICLES,
                                     centre: CENTRE,
                                     initialTemp: INIT_TEMP ) )
println "Starting Particle System"
new PAR ( network ).run()
